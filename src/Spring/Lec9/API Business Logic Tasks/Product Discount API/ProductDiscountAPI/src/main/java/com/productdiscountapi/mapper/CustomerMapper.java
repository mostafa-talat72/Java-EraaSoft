package com.productdiscountapi.mapper;

import com.productdiscountapi.dto.CustomerDTO;
import com.productdiscountapi.dto.CustomerRequestDTO;
import com.productdiscountapi.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDTO convertFromCustomerToCustomerDto(Customer customer);

    Customer convertFromCustomerRequestDtoToCustomer(CustomerRequestDTO requestDTO);

    @Mapping(target = "id", ignore = true)
    void updateCustomerFromCustomerRequestDto(CustomerRequestDTO requestDTO, @MappingTarget Customer customer);

    List<CustomerDTO> convertFromCustomerListToCustomerDtoList(List<Customer> customers);
}
