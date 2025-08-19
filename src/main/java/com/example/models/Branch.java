package com.example.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "BRANCHES", indexes = {@Index(name = "idx_branch_bic", columnList = "bic_code"), @Index(name = "idx_branch_bank", columnList = "bank_id")})
public class Branch implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @Column(name = "email", length = 100)
    @Email
    private String email;

    @Column(name = "telephone_number", length = 20)
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$")
    private String telephoneNumber;

    @Column(name = "bic_code", nullable = false, length = 11, unique = true)
    @NotNull
    @Pattern(regexp = "^[A-Z]{6}[A-Z0-9]{2}([A-Z0-9]{3})?$")
    private String bicCode;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    @JsonManagedReference
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    @JsonBackReference
    private Bank bank;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    @OrderBy("lastName ASC")
    @JsonManagedReference
    private Set<Customer> customers = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Version
    private Long version;

    public void addCustomer(Customer customer) {
        customers.add(customer);
        customer.setBranch(this);
    }

    public void removeCustomer(Customer customer) {
        customers.remove(customer);
        customer.setBranch(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Branch branch)) return false;
        return id != null && id.equals(branch.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
