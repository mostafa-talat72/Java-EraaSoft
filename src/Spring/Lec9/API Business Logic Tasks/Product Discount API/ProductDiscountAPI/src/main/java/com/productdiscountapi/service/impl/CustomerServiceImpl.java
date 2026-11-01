package com.productdiscountapi.service.impl;

import com.productdiscountapi.dto.CustomerDTO;
import com.productdiscountapi.dto.CustomerRequestDTO;
import com.productdiscountapi.exception.NotFoundException;
import com.productdiscountapi.mapper.CustomerMapper;
import com.productdiscountapi.model.Customer;
import com.productdiscountapi.repo.CustomerRepo;
import com.productdiscountapi.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepo customerRepo;
    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CustomerDTO> getAllCustomers() {
        // Read-only query to retrieve all customers.
        List<Customer> customers = customerRepo.findAll();

        if (customers.isEmpty()) {
            throw new NotFoundException("No customers found in the system");
        }

        return customerMapper.convertFromCustomerListToCustomerDtoList(customers);
    }

    @Transactional(readOnly = true)
    @Override
    public Customer getCustomerEntityById(Long id) {
        // Raw entity retrieval for internal service-level relations.
        return findCustomerOrThrow(id);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerDTO getCustomerById(Long id) {
        // Fetch customer entity and map to DTO.
        return customerMapper.convertFromCustomerToCustomerDto(findCustomerOrThrow(id));
    }

    @Override
    public CustomerDTO createCustomer(CustomerRequestDTO requestDTO) {
        // Map request payload to entity and persist.
        Customer customer = customerMapper.convertFromCustomerRequestDtoToCustomer(requestDTO);

        return customerMapper.convertFromCustomerToCustomerDto(customerRepo.save(customer));
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {
        // Fetch existing customer or throw 404.
        Customer customer = findCustomerOrThrow(id);

        // In-place update using MapStruct target mapping.
        customerMapper.updateCustomerFromCustomerRequestDto(requestDTO, customer);

        return customerMapper.convertFromCustomerToCustomerDto(customerRepo.save(customer));
    }

    @Override
    public void deleteCustomer(Long id) {
        // Ensure customer exists before deleting.
        Customer customer = findCustomerOrThrow(id);
        customerRepo.delete(customer);
    }

    // Shared helper for safe customer lookup.
    private Customer findCustomerOrThrow(Long id) {
        return customerRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Customer with id " + id + " not found"));
    }
}
