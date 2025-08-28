package com.example.services.impl;

import com.example.dtos.CustomerDto;
import com.example.dtos.overview.AccountOverviewDto;
import com.example.dtos.overview.BranchOverviewDto;
import com.example.dtos.overview.CustomerOverviewDto;
import com.example.dtos.overview.UserOverviewDto;
import com.example.entities.Account;
import com.example.entities.Branch;
import com.example.entities.Customer;
import com.example.entities.User;
import com.example.exceptions.domain.branch.BranchNotFoundException;
import com.example.exceptions.domain.customer.CustomerNotFoundException;
import com.example.mapers.AccountMapper;
import com.example.mapers.BranchMapper;
import com.example.mapers.CustomerMapper;
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
        Page<Customer> cardPage = customerRepository.findAll(pageable);
        return cardPage.map(customerMapper::toCustomerOverviewDto);
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        return customerMapper.toCustomerDto(customerRepository.save(customerMapper.toCustomer(customerDto)));
    }

    public CustomerOverviewDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        return customerMapper.toCustomerOverviewDto(customer);
    }

    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        Customer card = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        customerMapper.updateCustomer(card, customerMapper.toCustomer(customerDto));
        return customerMapper.toCustomerDto(customerRepository.save(card));
    }

    public void deleteCustomer(Long id) {
        customerRepository.delete(customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id)));
    }

    public Set<CustomerDto> getBranchCustomersByBranchId(Long branchId) {
        return null;
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
