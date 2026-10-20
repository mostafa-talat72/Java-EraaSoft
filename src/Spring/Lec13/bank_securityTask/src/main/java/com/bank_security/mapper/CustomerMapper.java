package com.bank_security.mapper;

import com.bank_security.dto.CustomerDTO;
import com.bank_security.dto.CustomerRequestDTO;
import com.bank_security.dto.CustomerResponseMeDTO;
import com.bank_security.dto.CustomerSimpleDTO;
import com.bank_security.dto.CustomerUpdateRequestDTO;
import com.bank_security.model.Customer;
import com.bank_security.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    // Nested account is slim (no back-reference), so no ignore needed.
    public Customer convertFromCustomerDtoToCustomer(CustomerDTO customerDTO);

    public CustomerDTO convertFromCustomerToCustomerDto(Customer customer);

    public List<Customer> convertFromCustomerDtoListToCustomerList(List<CustomerDTO> customerDtoList);

    public List<CustomerDTO> convertFromCustomerListToCustomerDtoList(List<Customer> customers);

    public Customer convertFromCustomerSimpleDtoToCustomer(CustomerSimpleDTO customerSimpleDTO);

    public CustomerSimpleDTO convertFromCustomerToCustomerSimpleDto(Customer customer);

    public List<Customer> convertFromCustomerSimpleDtoListToCustomerList(List<CustomerSimpleDTO> customerSimpleDtoList);

    public List<CustomerSimpleDTO> convertFromCustomerListToCustomerSimpleDtoList(List<Customer> customers);

    public Customer convertFromCustomerRequestDtoToCustomer(CustomerRequestDTO customerRequestDTO);

    public List<Customer> convertFromCustomerRequestDtoListToCustomerList(List<CustomerRequestDTO> customerRequestDtoList);

    public CustomerRequestDTO convertFromCustomerToCustomerRequestDto(Customer customer);

    public List<CustomerRequestDTO> convertFromCustomerListToCustomerRequestDtoList(List<Customer> customers);

    public Customer convertFromCustomerUpdateRequestDtoToCustomer(CustomerUpdateRequestDTO customerUpdateRequestDTO);

    public List<Customer> convertFromCustomerUpdateRequestDtoListToCustomerList(List<CustomerUpdateRequestDTO> customerUpdateRequestDtoList);

    public CustomerUpdateRequestDTO convertFromCustomerToCustomerUpdateRequestDto(Customer customer);

    public List<CustomerUpdateRequestDTO> convertFromCustomerListToCustomerUpdateRequestDtoList(List<Customer> customers);

    // roles/authorities are List<String> names filled from the security
    // principal in CustomerServiceImpl.getMe, not from the entity.
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    public CustomerResponseMeDTO convertFromCustomerToCustomerResponseMeDto(Customer customer);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    public List<CustomerResponseMeDTO> convertFromCustomerListToCustomerResponseMeDtoList(List<Customer> customers);

    // Direction DTO -> entity: role names cannot resolve to entities here.
    @Mapping(target = "roles", ignore = true)
    public Customer convertFromCustomerResponseMeDtoToCustomer(CustomerResponseMeDTO customerResponseMeDTO);

    @Mapping(target = "roles", ignore = true)
    public List<Customer> convertFromCustomerResponseMeDtoListToCustomerList(List<CustomerResponseMeDTO> customerResponseMeDtoList);

}
