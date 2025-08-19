package com.example.services;

import com.example.dtos.CustomerDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.exceptions.AppException;
import com.example.mapers.CustomerMapper;
import com.example.models.Account;
import com.example.models.Customer;
import com.example.models.User;
import com.example.repository.AccountRepository;
import com.example.repository.CustomerRepository;
import com.example.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, AccountRepository accountRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Set<CustomerOverviewDto> allCustomers() {
        return customerMapper.toCustomerOverviewDtos(new HashSet<>(customerRepository.findAll()));
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

    public CustomerDto getCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        return customerMapper.toCustomerDto(customer);
    }


    public CustomerDto addAccountToCustomer(Long customerId, Long accountId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException("Account not found", HttpStatus.NOT_FOUND));
        if (account.getCustomer() != null) {
            throw new AppException("Account already is owned by a costumer", HttpStatus.CONFLICT);
        }
        customer.addAccount(account);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDto(savedCustomer);
    }

    public CustomerDto removeAccountFromCustomer(Long customerId, Long accountId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AppException("Account not found", HttpStatus.NOT_FOUND));
        if (!customer.getAccounts().contains(account)) {
            throw new AppException("Account is not owned by a this costumer", HttpStatus.CONFLICT);
        }
        customer.getAccounts().remove(account);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDto(savedCustomer);
    }

    public CustomerDto setUserToCustomer(Long customerId, Long userId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        if (user.getCustomer() != null) {
            throw new AppException("User is owned by this costumer", HttpStatus.CONFLICT);
        }
        customer.setUser(user);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDto(savedCustomer);
    }

    public CustomerDto removeUserFromCustomer(Long customerId, Long userId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new AppException("Customer not found", HttpStatus.NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        if (!customer.getUser().equals(user)) {
            throw new AppException("User is not owned by this costumer", HttpStatus.CONFLICT);
        }
        customer.setUser(null);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDto(savedCustomer);
    }
}
