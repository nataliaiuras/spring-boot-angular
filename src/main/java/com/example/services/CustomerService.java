package com.example.services;

import com.example.dtos.AccountDto;
import com.example.dtos.CustomerDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.models.Customer;
import com.example.exceptions.AppException;
import com.example.mapers.CustomerMapper;
import com.example.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public List<CustomerOverviewDto> allCustomers() {
        return customerMapper.toCustomerOverviewDtos(customerRepository.findAll());
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = customerMapper.toCustomer(customerDto);

        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toCustomerDto(savedCustomer);
    }

    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        customerMapper.updateCustomer(customer, customerMapper
                .toCustomer(customerDto));
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDto(savedCustomer);
    }

    public CustomerDto patchCustomer(Long id, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));

        if (customerDto.getFirstName() != null) {
            customer.setFirstName(customerDto.getFirstName());
        }
        if (customerDto.getLastName() != null) {
            customer.setLastName(customerDto.getLastName());
        }
        if (customerDto.getBirthDate() != null) {
            customer.setBirthDate(customerDto.getBirthDate());
        }
        if (customerDto.getCnp() != null) {
            customer.setCnp(customerDto.getCnp());
        }
        if (customerDto.getTelephoneNumber() != null) {
            customer.setTelephoneNumber(customerDto.getTelephoneNumber());
        }
        if (customerDto.getEmail() != null) {
            customer.setEmail(customerDto.getEmail());
        }

        Customer savedCustomer = customerRepository.save(customer);

        return customerMapper.toCustomerDto(savedCustomer);
    }

    public CustomerDto deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        CustomerDto customerDto = customerMapper.toCustomerDto(customer);

        customerRepository.deleteById(id);

        return customerDto;
    }

    @Transactional
    public CustomerDto getCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
       /* customer.getBranch().getBank().getName();*/
        return customerMapper.toCustomerDto(customer);
    }


    public List<AccountDto> getAllAccounts(Long id) {
        return null;
    }

}
