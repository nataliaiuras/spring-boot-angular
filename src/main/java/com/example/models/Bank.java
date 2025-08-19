package com.example.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.validator.constraints.URL;
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
@Table(name = "BANKS", indexes = {@Index(name = "idx_bank_name", columnList = "name")})
public class Bank implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, length = 100)
    @NotNull
    @Size(min = 2, max = 100)
    private String name;

    @Column(name = "website", length = 255)
    @URL
    private String website;

    @OneToMany(mappedBy = "bank", fetch = FetchType.LAZY)
    @BatchSize(size = 20)
    @JsonManagedReference
    private Set<Branch> branches = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Version
    private Long version;

    public void addBranch(Branch branch) {
        this.branches.add(branch);
        branch.setBank(this);
    }

    public void removeBranch(Branch branch) {
        this.branches.remove(branch);
        branch.setBank(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Bank bank)) return false;
        return id != null && id.equals(bank.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
