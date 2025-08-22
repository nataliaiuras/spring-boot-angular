package com.example.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ADDRESSES", indexes = {@Index(name = "idx_address_postal", columnList = "postal_code,country")})
@EntityListeners(AuditingEntityListener.class)
public class Address implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Min(1)
    private int number;

    @Column(nullable = false, length = 100)
    @NotNull
    @Size(min = 2, max = 100)
    private String street;

    @Column(nullable = false, length = 100)
    @NotNull
    @Size(min = 2, max = 100)
    private String city;

    @Column(length = 100)
    @Size(max = 100)
    private String county;

    @Column(nullable = false, length = 2)
    @NotNull
    @Pattern(regexp = "^[A-Z]{2}$")
    private String country;

    @Column(name = "postal_code", nullable = false)
    @Pattern(regexp = "^[0-9]{4,10}$")
    private String postalCode;

    @OneToOne(mappedBy = "address")
    @JsonBackReference
    private Branch branch;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Version
    private Long version;

}
