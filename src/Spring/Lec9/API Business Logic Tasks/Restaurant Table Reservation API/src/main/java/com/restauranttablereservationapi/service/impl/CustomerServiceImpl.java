package com.restauranttablereservationapi.service.impl;

import com.restauranttablereservationapi.dto.CustomerDTO;
import com.restauranttablereservationapi.dto.CustomerRequestDTO;
import com.restauranttablereservationapi.exception.BadRequestException;
import com.restauranttablereservationapi.exception.NotFoundException;
import com.restauranttablereservationapi.helper.ReservationHelper;
import com.restauranttablereservationapi.mapper.CustomerMapper;
import com.restauranttablereservationapi.model.Customer;
import com.restauranttablereservationapi.repo.CustomerRepo;
import com.restauranttablereservationapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Transactional
@Validated
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;

    // Only used to refuse deleting a customer that appears in RT_RESERVATION.
    private final ReservationHelper reservationHelper;

    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CustomerDTO> getAllCustomers() {
        List<Customer> customers = customerRepo.findAllByOrderByIdAsc();

        // Same convention as the previous tasks: an empty list is a 404.
        if (customers.isEmpty()) {
            throw new NotFoundException("No customers found in the system");
        }

        return customerMapper.convertFromCustomerListToCustomerDtoList(customers);
    }

    @Override
    public Customer getCustomerEntityById(Long id) {
        return findCustomerOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerDTO getCustomerById(Long id) {
        return customerMapper.convertFromCustomerToCustomerDto(findCustomerOrThrow(id));
    }

    @Override
    public CustomerDTO createCustomer(CustomerRequestDTO requestDTO) {
        // Pre-check the unique business key so a duplicate becomes a clear 400
        // instead of a raw DB unique-constraint 500.
        if (customerRepo.existsByPhone(requestDTO.getPhone())) {
            throw new BadRequestException(
                    "Customer with phone '" + requestDTO.getPhone() + "' already exists");
        }

        Customer customer = customerMapper.convertFromCustomerRequestDtoToCustomer(requestDTO);

        return customerMapper.convertFromCustomerToCustomerDto(customerRepo.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {
        // Managed entity: dirty checking flushes the new values at commit.
        Customer customer = findCustomerOrThrow(id);

        if (customerRepo.existsByPhoneAndIdNot(requestDTO.getPhone(), id)) {
            throw new BadRequestException(
                    "Customer with phone '" + requestDTO.getPhone() + "' already exists");
        }

        customerMapper.updateCustomerFromCustomerRequestDto(requestDTO, customer);

        return customerMapper.convertFromCustomerToCustomerDto(customer);
    }

    @Override
    public void deleteCustomer(Long id) {
        Customer customer = findCustomerOrThrow(id);

        // Refuse the delete while reservations still reference this customer,
        // otherwise the FK constraint fails and the client gets a 500.
        if (reservationHelper.customerHasReservationHistory(id)) {
            throw new BadRequestException(
                    "Customer with id " + id + " has reservations and cannot be deleted");
        }

        customerRepo.delete(customer);
    }

    // Shared helper for safe entity retrieval with consistent 404 messaging.
    private Customer findCustomerOrThrow(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));
    }
}
