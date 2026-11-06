package com.restauranttablereservationapi.service;

import com.restauranttablereservationapi.dto.CustomerDTO;
import com.restauranttablereservationapi.dto.CustomerRequestDTO;
import com.restauranttablereservationapi.model.Customer;
import jakarta.validation.Valid;

import java.util.List;

public interface CustomerService {

    // @Valid on the contract, not on the implementation: Hibernate Validator
    // forbids an override from redefining the parameter constraints (HV000151).
    CustomerDTO createCustomer(@Valid CustomerRequestDTO requestDTO);

    CustomerDTO updateCustomer(Long id, @Valid CustomerRequestDTO requestDTO);

    List<CustomerDTO> getAllCustomers();

    // Raw entity for internal callers (e.g. ReservationServiceImpl).
    Customer getCustomerEntityById(Long id);

    CustomerDTO getCustomerById(Long id);

    void deleteCustomer(Long id);
}
