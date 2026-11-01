package com.productdiscountapi.mapper;

import com.productdiscountapi.dto.DiscountCodeDTO;
import com.productdiscountapi.dto.DiscountCodeRequestDTO;
import com.productdiscountapi.dto.DiscountCodeSimpleDTO;
import com.productdiscountapi.model.DiscountCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DiscountCodeMapper {

    DiscountCodeDTO convertFromDiscountCodeToDiscountCodeDto(DiscountCode discountCode);

    DiscountCodeSimpleDTO convertFromDiscountCodeToDiscountCodeSimpleDto(DiscountCode discountCode);

    DiscountCode convertFromDiscountCodeRequestDtoToDiscountCode(DiscountCodeRequestDTO requestDTO);

    List<DiscountCodeDTO> convertFromDiscountCodeListToDiscountCodeDtoList(List<DiscountCode> discountCodes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "incompatibleCodes",  ignore = true)
    void updateDiscountCodeFromDiscountCodeRequestDto(DiscountCodeRequestDTO requestDTO, @MappingTarget DiscountCode discountCode);
}
