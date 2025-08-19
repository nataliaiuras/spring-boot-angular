package com.example.models;

import com.example.utils.MaskSensitive;
import com.example.utils.Role;
import com.example.utils.SensitiveDataSerializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "USERS", indexes = {@Index(name = "idx_user_username", columnList = "username", unique = true)})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @NotNull
    @Size(min = 4, max = 50)
    private String username;

    @Column(nullable = false)
    @NotNull
    @Size(min = 60, max = 60) // For BCrypt
    @MaskSensitive(maskWith = "*******")
    @JsonSerialize(using = SensitiveDataSerializer.class)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(mappedBy = "user")
    @JsonBackReference
    private Customer customer;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedDate
    private Instant lastModifiedDate;

    @Version
    private Long version;

    @PrePersist
    @PreUpdate
    private void encryptPassword() {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        if (password != null && !password.startsWith("$2a$")) {
            password = encoder.encode(password);
        }
    }

}