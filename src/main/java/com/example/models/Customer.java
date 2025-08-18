package com.example.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CUSTOMERS", indexes = {
        @Index(name = "idx_customer_cnp", columnList = "cnp", unique = true),
        @Index(name = "idx_customer_email", columnList = "email", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class Customer implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    @NotNull
    @Size(min = 2, max = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    @NotNull
    @Size(min = 2, max = 50)
    private String lastName;

    @Column(name = "birth_date", nullable = false)
    @Past
    @Temporal(TemporalType.DATE)
    private LocalDate birthDate;

    @Column(unique = true, length = 13)
    @Pattern(regexp = "^[0-9]{13}$")
    private String cnp;

    @Column(name = "telephone_number", length = 20)
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$")
    private String telephoneNumber;

    @Column(unique = true, length = 100)
    @Email
    private String email;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    @JsonManagedReference
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    @NotNull
    @JsonBackReference
    private Branch branch;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    @JsonManagedReference
    private Set<Account> accounts = new HashSet<>();

    @CreatedDate
    @Column(updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Version
    private Long version;

    public void addAccount(Account account) {
        if (account != null) {
            accounts.add(account);
            account.setCustomer(this);
        }
    }

    public void removeAccount(Account account) {
        accounts.remove(account);
        account.setCustomer(null);
    }


}
