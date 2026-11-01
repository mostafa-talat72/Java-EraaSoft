package com.productdiscountapi.service;

import com.productdiscountapi.dto.CustomerDTO;
import com.productdiscountapi.dto.CustomerRequestDTO;
import com.productdiscountapi.model.Customer;

import java.util.List;

public interface CustomerService {

    List<CustomerDTO> getAllCustomers();

    Customer getCustomerEntityById(Long id);

    CustomerDTO getCustomerById(Long id);

    CustomerDTO createCustomer(CustomerRequestDTO requestDTO);

    CustomerDTO updateCustomer(Long id, CustomerRequestDTO requestDTO);

    void deleteCustomer(Long id);
}
