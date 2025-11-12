package com.example.service.impl;

import com.example.dto.request.customer.CustomerCreateRequest;
import com.example.dto.request.customer.CustomerUpdateRequest;
import com.example.dto.response.customer.CustomerDetailResponse;
import com.example.dto.response.customer.CustomerResponse;
import com.example.dto.response.account.AccountResponse;
import com.example.dto.response.branch.BranchResponse;
import com.example.dto.response.user.UserResponse;
import com.example.entity.Account;
import com.example.entity.Branch;
import com.example.entity.Customer;
import com.example.entity.User;
import com.example.exception.domain.customer.CustomerNotFoundException;
import com.example.util.enums.CardStatus;
import com.example.mapper.AccountMapper;
import com.example.mapper.BranchMapper;
import com.example.mapper.CustomerMapper;
import com.example.repository.CustomerRepository;
import com.example.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final BranchMapper branchMapper;
    private final AccountMapper accountMapper;

    public Page<CustomerResponse> getAllCustomers(Pageable pageable) {
        Page<Customer> page = customerRepository.findAllActive(pageable);
        return page.map(customerMapper::toCustomerResponse);
    }

    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        return customerMapper.toCustomerResponse(customer);
    }

    public CustomerDetailResponse createCustomer(CustomerCreateRequest request) {
        Customer customer = customerRepository.save(customerMapper.toCustomer(request));
        return customerMapper.toCustomerDetailResponse(customer);
    }

    public CustomerDetailResponse updateCustomer(Long id, CustomerUpdateRequest request) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        customerMapper.updateCustomer(customer, customerMapper.toCustomer(request));
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDetailResponse(savedCustomer);
    }

    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        customerRepository.delete(customer);
    }

    public CustomerDetailResponse deactivateCustomer(Long id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        // Set customer as inactive
        customer.setActive(false);

        // Deactivate all customer's accounts
        customer.getAccounts().forEach(account -> {
            if (account.isActive()) {
                account.setActive(false);

                // Block all cards associated with the accounts
                account.getCards().forEach(card -> {
                    card.setStatus(CardStatus.BLOCKED);
                    card.setBlockedReason("Customer account deactivated");
                });
            }
        });

        customerRepository.save(customer);
        return customerMapper.toCustomerDetailResponse(customer);
    }

    public CustomerDetailResponse reactivateCustomer(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        customer.setActive(true);
        customerRepository.save(customer);
        return customerMapper.toCustomerDetailResponse(customer);
    }


    @Override
    public Set<AccountResponse> getAccountsByCustomerId(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        Set<Account> accounts = customer.getAccounts();

        Set<AccountResponse> accountResponses = new HashSet<>();
        if (!accounts.isEmpty()) {
            accountResponses.add(accountMapper.toAccountResponse(accounts.iterator().next()));
        }
        return accountResponses;
    }

    @Override
    public BranchResponse getBranchByCustomerId(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        Branch branch = customer.getBranch();
        return branchMapper.toBranchResponse(branch);
    }

    @Override
    public UserResponse getUserByCustomerId(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
        User user = customer.getUser();
        return new UserResponse(user.getId(), user.getUsername(), user.getRole());
    }
}
