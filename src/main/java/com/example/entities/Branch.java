package com.example.entities;

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

    @Column(name = "branch_code", nullable = false, length = 3, unique = true)
    @NotNull
    @Pattern(regexp = "^[A-Z0-9]{3}$")
    private String branchCode;

    @Column(name = "location_code", nullable = false, length = 2, unique = true)
    @NotNull
    @Pattern(regexp = "^\\d{2}$")
    private String locationCode;

    @Column(name = "bic_code", nullable = false, length = 11, unique = true)
    @NotNull
    @Pattern(regexp = "^[A-Z]{6}\\d{2}([A-Z0-9]{3})?$", message = "Invalid BIC code format (DEUTDE22A30 or DEUTDE22XXX)")
    private String bicCode;

    @Column(name = "name", unique = true, nullable = false, length = 100)
    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @Column(name = "email", unique = true, length = 100)
    @Email
    private String email;

    @Column(name = "telephone_number", length = 20, unique = true)
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in international format (+1234567890)")
    private String telephoneNumber;

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


}
