package com.example.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BRANCHES")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "BIC_CODE")
    private String bicCode;

    @Column(name = "SWIFT_CODE")
    private String swiftCode;

    @OneToOne
    private Address address;

    @Column(name = "TELEPHONE_NUMBER")
    private String telephoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @ManyToOne
    @JoinColumn(name = "bank_id")
    @JsonBackReference
    private Bank bank;

    @OneToMany(mappedBy = "branch")
    @JsonManagedReference
    private List<Client> clients;

}
