package com.productdiscountapi.service.impl;

import com.productdiscountapi.dto.DiscountCalculationRequest;
import com.productdiscountapi.dto.DiscountCalculationResponse;
import com.productdiscountapi.dto.DiscountItemRequest;
import com.productdiscountapi.exception.BadRequestException;
import com.productdiscountapi.model.Customer;
import com.productdiscountapi.model.DiscountCode;
import com.productdiscountapi.model.Product;
import com.productdiscountapi.service.CustomerService;
import com.productdiscountapi.service.DiscountCalculationService;
import com.productdiscountapi.service.DiscountCodeService;
import com.productdiscountapi.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiscountCalculationServiceImpl implements DiscountCalculationService {

    // Percentages are whole numbers, money is always BigDecimal.
    private static final int VIP_EXTRA_PERCENTAGE = 5;
    private static final int MAX_TOTAL_DISCOUNT_PERCENTAGE = 30;
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = BigDecimal.valueOf(1000);
    private static final BigDecimal SHIPPING_FEE = BigDecimal.valueOf(50);
    private static final int MONEY_SCALE = 2;

    private final ProductService productService;
    private final CustomerService customerService;
    private final DiscountCodeService discountCodeService;

    // Result of one order line: its gross value and the discount applied to it.
    // A record is immutable, so it is used only as a return value, never as an accumulator.
    private record TotalSummary(BigDecimal subtotalPrice, BigDecimal totalDiscount) {
    }

    @Override
    public DiscountCalculationResponse calculateDiscount(DiscountCalculationRequest request) {

        // Rule 6: resolve the submitted codes first, then reject incompatible combinations.
        List<DiscountCode> discountCodes = loadDiscountCodes(request.getDiscountCodes());
        validateCodesCompatibility(discountCodes);
        int discountCodesPercentage = sumDiscountCodePercentage(discountCodes);

        // Rule 3: VIP customers get an extra percentage on eligible products only.
        Customer customer = customerService.getCustomerEntityById(request.getCustomerId());
        boolean vip = Boolean.TRUE.equals(customer.getVip());

        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal totalDiscount = BigDecimal.ZERO;

        for (DiscountItemRequest itemRequest : request.getItems()) {

            Product product = productService.getProductEntityById(itemRequest.getProductId());

            // Rules 2, 3, 4 and 5: category + codes + VIP for eligible products,
            // capped at 30% per line, so the order discount can never exceed 30% either.
            int productPercentage = calculateProductDiscountPercentage(
                    product, discountCodesPercentage, vip);

            TotalSummary lineSummary = calculateTotalProductPrice(
                    product, productPercentage, itemRequest.getQuantity());

            subtotal = subtotal.add(lineSummary.subtotalPrice());
            totalDiscount = totalDiscount.add(lineSummary.totalDiscount());
        }

        subtotal = money(subtotal);
        totalDiscount = money(totalDiscount);

        // Free shipping is decided by the amount the customer pays after discounts.
        BigDecimal discountedSubtotal = money(subtotal.subtract(totalDiscount));
        boolean freeShipping = discountedSubtotal.compareTo(FREE_SHIPPING_THRESHOLD) >= 0;
        BigDecimal shippingCost = freeShipping ? money(BigDecimal.ZERO) : money(SHIPPING_FEE);

        BigDecimal finalPrice = money(discountedSubtotal.add(shippingCost));

        return new DiscountCalculationResponse(
                subtotal,
                totalDiscount,
                shippingCost,
                finalPrice
        );
    }

    /**
     * Trims and resolves the submitted discount codes.
     * Blank entries and repeated codes are rejected with 400.
     * Unknown codes are reported by the discount code service as 404.
     */
    private List<DiscountCode> loadDiscountCodes(List<String> codes) {

        if (codes == null || codes.isEmpty()) {
            return List.of();
        }

        Set<String> uniqueCodes = new HashSet<>();

        return codes.stream()
                .map(rawCode -> {
                    if (rawCode == null || rawCode.isBlank()) {
                        throw new BadRequestException("Discount code cannot be empty");
                    }

                    String code = rawCode.trim();

                    if (!uniqueCodes.add(code)) {
                        throw new BadRequestException(
                                "Discount code '" + code + "' is provided more than once");
                    }

                    return code;
                })
                .map(discountCodeService::getDiscountCodeEntityByCode)
                .toList();
    }

    /**
     * Rule 6: two incompatible codes cannot be combined.
     * Every submitted code is inspected, so the conflict is caught no matter
     * which side declared the relation in the database.
     */
    private void validateCodesCompatibility(List<DiscountCode> discountCodes) {

        if (discountCodes.size() < 2) {
            return;
        }

        Set<Long> codeIds = discountCodes.stream()
                .map(DiscountCode::getId)
                .collect(Collectors.toSet());

        for (DiscountCode discountCode : discountCodes) {

            for (DiscountCode incompatibleCode : discountCode.getIncompatibleCodes()) {

                if (Objects.isNull(incompatibleCode)
                        || incompatibleCode.getId().equals(discountCode.getId())) {
                    continue;
                }

                if (codeIds.contains(incompatibleCode.getId())) {
                    throw new BadRequestException(
                            "Discount codes '" + discountCode.getCode() + "' and '"
                                    + incompatibleCode.getCode() + "' cannot be combined");
                }
            }
        }
    }

    /**
     * Compatible codes stack, so their whole percentages are summed.
     * Duplicates were already rejected, so no percentage is counted twice.
     */
    private int sumDiscountCodePercentage(List<DiscountCode> discountCodes) {

        List<Integer> codePercentages = discountCodes.stream()
                .map(DiscountCode::getDiscountPercentage)
                .filter(Objects::nonNull)
                .toList();

        int total = 0;

        for (Integer codePercentage : codePercentages) {
            total += codePercentage;
        }

        return total;
    }

    /**
     * Combines the rules for one product:
     * category percentage + discount codes + VIP percentage.
     * Non-eligible products always get 0, and the result is capped at 30%.
     */
    private int calculateProductDiscountPercentage(Product product,
                                                   int discountCodePercentage,
                                                   boolean vip) {

        if (!Boolean.TRUE.equals(product.getDiscountEligible())) {
            return 0;
        }

        int categoryPercentage = Objects.nonNull(product.getCategory())
                && Objects.nonNull(product.getCategory().getDiscountPercentage())
                ? product.getCategory().getDiscountPercentage()
                : 0;

        int combinedPercentage = categoryPercentage + discountCodePercentage;

        // Rule 3: the extra VIP percentage is added only when the flag is true.
        if (vip) {
            combinedPercentage += VIP_EXTRA_PERCENTAGE;
        }

        return Math.min(MAX_TOTAL_DISCOUNT_PERCENTAGE, combinedPercentage);
    }

    /**
     * Calculates one order line: its gross value and the discount amount applied to it.
     * The discount is derived from the rounded line value, then rounded to money scale.
     */
    private TotalSummary calculateTotalProductPrice(Product product,
                                                    int productDiscountPercentage,
                                                    Integer quantity) {

        BigDecimal lineSubtotal = money(
                product.getPrice().multiply(BigDecimal.valueOf(quantity)));

        BigDecimal lineDiscount = percentageOf(lineSubtotal, productDiscountPercentage);

        return new TotalSummary(lineSubtotal, lineDiscount);
    }

    /**
     * Converts a whole percentage (15) into a rate (0.15) and returns the rounded amount.
     * Shifting the decimal point avoids dividing by 100 with the wrong scale.
     */
    private BigDecimal percentageOf(BigDecimal amount, int percentage) {
        return money(amount.multiply(BigDecimal.valueOf(percentage).movePointLeft(2)));
    }

    /**
     * Rounds every monetary value to two decimals using commercial rounding.
     */
    private BigDecimal money(BigDecimal amount) {
        return amount.setScale(MONEY_SCALE, RoundingMode.HALF_UP);
    }
}
