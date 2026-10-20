package com.bank_security.service.impl;

import com.bank_security.dto.*;
import com.bank_security.exception.CustomException;
import com.bank_security.helper.CustomUserDetails;
import com.bank_security.mapper.CustomerMapper;
import com.bank_security.model.AccountStatus;
import com.bank_security.model.Customer;
import com.bank_security.model.Role;
import com.bank_security.repo.CustomerRepo;
import com.bank_security.service.AccountStatusService;
import com.bank_security.service.CustomerService;
import com.bank_security.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

    private CustomerRepo customerRepo;

    private CustomerMapper customerMapper;

    private RoleService roleService;

    private AccountStatusService accountStatusService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImpl(CustomerRepo customerRepo, CustomerMapper customerMapper, RoleService roleService, AccountStatusService accountStatusService, PasswordEncoder passwordEncoder) {
        this.customerRepo = customerRepo;
        this.customerMapper = customerMapper;
        this.roleService = roleService;
        this.accountStatusService = accountStatusService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Customer getCustomerEntityByEmail(String email) {
        return customerRepo.findByEmail(email).orElseThrow(
                ()-> new UsernameNotFoundException("The customer does not exist with email: " + email)
        );
    }

    @Override
    public CustomerDTO getCustomerByEmail(String email) {
       return customerMapper.convertFromCustomerToCustomerDto(getCustomerEntityByEmail(email));
    }

    @Override
    public CustomerSimpleDTO registration(CustomerRequestDTO customerRequestDTO) {

        if(Objects.nonNull(customerRequestDTO.getId())){
            throw new CustomException(
                    "Id non null Exception",
                    "Id must be null to create customer"
            );
        }

        if(customerRepo.existsByEmail(customerRequestDTO.getEmail())){
            throw new CustomException(
                    "Duplicate Email Exception",
                    "Email already exists: " + customerRequestDTO.getEmail()
            );
        }

        String password = passwordEncoder.encode(customerRequestDTO.getPassword());

        Customer customer =   customerMapper.convertFromCustomerRequestDtoToCustomer(customerRequestDTO);

        customer.setEnabled(true);
        customer.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));
        Role role = roleService.findByName("CUSTOMER");
        customer.setRoles(List.of(role));
        customer.setPassword(password);

        return customerMapper.convertFromCustomerToCustomerSimpleDto(
               customerRepo.save(customer)
        );

    }

    @Override
    public Customer getCustomerEntityById(long id) {
        return customerRepo.findById(id).orElseThrow(
                () -> new CustomException(
                        "Id Not found Exception",
                        "Customer not found with this id: " + id
                )
        );
    }

    @Override
    public CustomerSimpleDTO getCustomerById(long id) {
        return customerMapper.convertFromCustomerToCustomerSimpleDto(
                getCustomerEntityById(id)
        );
    }

    @Override
    public CustomerSimpleDTO updateCustomer(long id, CustomerUpdateRequestDTO customerUpdateRequestDTO) {
        if(Objects.isNull(customerUpdateRequestDTO.getId())){
            customerUpdateRequestDTO.setId(id);
        }

        if(id != customerUpdateRequestDTO.getId()){
            throw new CustomException(
                    "Id not equals Exception",
                    "Customer id must be equal path variable id"
            );
        }


        Customer customer = customerRepo.findById(id).orElseThrow(
                () -> new CustomException(
                "Id not found Exception",
                "Customer not found with this id: " + id
                )
        );


        if (Objects.nonNull(customerUpdateRequestDTO.getEmail())) {
            if (customerRepo.existsByEmailAndIdNot(customerUpdateRequestDTO.getEmail(), id)) {
                throw new CustomException(
                        "Duplicate Email Exception",
                        "Email already exists: " + customerUpdateRequestDTO.getEmail()
                );
            }
            customer.setEmail(customerUpdateRequestDTO.getEmail());

        }

        if(Objects.nonNull(customerUpdateRequestDTO.getFullName())){
            customer.setFullName(customerUpdateRequestDTO.getFullName());
        }

        if(Objects.nonNull(customerUpdateRequestDTO.getPhone())){
            customer.setPhone(customerUpdateRequestDTO.getPhone());
        }

        if(Objects.nonNull(customerUpdateRequestDTO.getPassword())){
            customer.setPassword(
                    passwordEncoder.encode(customerUpdateRequestDTO.getPassword())
            );
        }

        return customerMapper.convertFromCustomerToCustomerSimpleDto(
                customerRepo.save(
                        customer
                )
        );

    }

    @Override
    public CustomerResponseMeDTO getMe(CustomUserDetails customUserDetails) {
        Customer customer = getCustomerEntityByEmail(customUserDetails.getUsername());
        CustomerResponseMeDTO customerResponseMeDTO = customerMapper.convertFromCustomerToCustomerResponseMeDto(customer);

        customerResponseMeDTO.setRoles(
                customUserDetails.getAuthorities().stream()
                        .filter(auth -> auth.getAuthority().startsWith("ROLE_"))
                        .map(auth -> auth.getAuthority().substring(5))
                        .toList()
        );

        customerResponseMeDTO.setAuthorities(
                customUserDetails.getAuthorities().stream()
                        .filter(auth -> !auth.getAuthority().startsWith("ROLE_"))
                        .map(auth -> auth.getAuthority())
                        .toList()
        );

        return customerResponseMeDTO;
    }

    @Override
    public CustomerSimpleDTO blockCustomerById(long id) {
        Customer customer = getCustomerEntityById(id);

        if(Objects.isNull(customer.getAccount())){
            throw new CustomException(
                    "Account Not found Exception",
                    "Account not found in this customer id: " + id
            );
        }

        if("BLOCKED".equals(customer.getAccount().getStatus().getName())){
            throw new CustomException(
                    "Account blocked Exception",
                    "Account is already blocked"
            );
        }

        AccountStatus accountStatus = accountStatusService.getAccountStatusEntityByName("BLOCKED");

        customer.getAccount().setStatus(accountStatus);

        return customerMapper.convertFromCustomerToCustomerSimpleDto(customerRepo.save(customer));
    }

    @Override
    public CustomerSimpleDTO unblockCustomerById(long id) {
        Customer customer = getCustomerEntityById(id);

        if(Objects.isNull(customer.getAccount())){
            throw new CustomException(
                    "Account Not found Exception",
                    "Account not found in this customer id: " + id
            );
        }

        if(!"BLOCKED".equals(customer.getAccount().getStatus().getName())){
            throw new CustomException(
                    "Account unblock Exception",
                    "Account is not blocked"
            );
        }

        AccountStatus accountStatus = accountStatusService.getAccountStatusEntityByName("ACTIVE");

        customer.getAccount().setStatus(accountStatus);

        return customerMapper.convertFromCustomerToCustomerSimpleDto(customerRepo.save(customer));
    }

    @Override
    public CustomerSimpleDTO disableCustomerById(long id) {
        Customer customer = getCustomerEntityById(id);

        if(!customer.getEnabled()){
            throw new CustomException(
                    "Customer disable Exception",
                    "Customer is already disabled"
            );
        }

        customer.setEnabled(false);
        return customerMapper.convertFromCustomerToCustomerSimpleDto(customerRepo.save(customer));
    }

    @Override
    public CustomerSimpleDTO enableCustomerById(long id) {
        Customer customer = getCustomerEntityById(id);

        if(customer.getEnabled()){
            throw new CustomException(
                    "Customer enable Exception",
                    "Customer is already enabled"
            );
        }

        customer.setEnabled(true);
        return customerMapper.convertFromCustomerToCustomerSimpleDto(customerRepo.save(customer));
    }

    @Override
    public Map<String, Long> getCustomerStatistics() {
        Map<String, Long> statistics = new HashMap<>();

        statistics.put("totalCustomers", customerRepo.count());
        statistics.put("enabledCustomers", customerRepo.countByEnabledTrue());

        return statistics;
    }
}
