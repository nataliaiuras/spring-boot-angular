package com.example.repository;

import com.example.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

        @Query("SELECT c FROM Customer c WHERE c.active = true")
        Page<Customer> findAllActive(Pageable pageable);

}
