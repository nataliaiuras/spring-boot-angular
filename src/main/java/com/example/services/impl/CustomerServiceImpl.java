package com.example.services.impl;

import com.example.models.dtos.customer.CustomerDto;
import com.example.models.dtos.customer.CustomerOverviewDto;
import com.example.models.dtos.customer.CustomerRequestDto;
import com.example.models.dtos.account.AccountOverviewDto;
import com.example.models.dtos.branch.BranchOverviewDto;
import com.example.models.dtos.user.UserOverviewDto;
import com.example.models.entities.Account;
import com.example.models.entities.Branch;
import com.example.models.entities.Customer;
import com.example.models.entities.User;
import com.example.exceptions.domain.customer.CustomerNotFoundException;
import com.example.utils.mapers.AccountMapper;
import com.example.utils.mapers.BranchMapper;
import com.example.utils.mapers.CustomerMapper;
import com.example.repository.CustomerRepository;
import com.example.services.CustomerService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final BranchMapper branchMapper;
    private final AccountMapper accountMapper;

    public Page<CustomerOverviewDto> getAllCustomers(Pageable pageable) {
        Page<Customer> page = customerRepository.findAll(pageable);
        return page.map(customerMapper::toOverviewDto);
    }

    public CustomerOverviewDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        return customerMapper.toOverviewDto(customer);
    }

    public CustomerDto createCustomer(CustomerRequestDto dto) {
        Customer customer = customerRepository.save(customerMapper.toCustomer(dto));
        return customerMapper.toCustomerDto(customer);
    }

    public CustomerDto updateCustomer(Long id, CustomerRequestDto dto) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        customerMapper.updateCustomer(customer, customerMapper.toCustomer(dto));
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDto(savedCustomer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        customerRepository.delete(customer);
    }

    @Override
    public Set<AccountOverviewDto> getAccountsByCustomerId(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        Set<Account> accounts = customer.getAccounts();
        return accountMapper.toAccountOverviewDtos(accounts);
    }

    @Override
    public BranchOverviewDto getBranchByCustomerId(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        Branch branch = customer.getBranch();
        return branchMapper.toBranchOverviewDto(branch);
    }

    @Override
    public UserOverviewDto getUserByCustomerId(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        User user = customer.getUser();
        return new UserOverviewDto(user.getId(), user.getUsername(), user.getRole());
    }
}
