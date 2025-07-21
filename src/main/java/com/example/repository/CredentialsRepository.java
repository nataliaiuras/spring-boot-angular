package com.example.repository;

import com.example.models.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository <Credentials, Long>{
}
