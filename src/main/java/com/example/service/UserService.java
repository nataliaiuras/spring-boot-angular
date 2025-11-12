package com.example.service;

import com.example.dto.request.user.PasswordUpdateRequest;
import com.example.dto.request.user.RoleUpdateRequest;
import com.example.dto.request.user.UserCreateRequest;
import com.example.dto.response.user.UserDetailResponse;
import com.example.dto.response.general.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.nio.file.AccessDeniedException;

public interface UserService {

    Long register(@Valid UserCreateRequest userCreateRequest);

    String authenticate(@NotNull @NotEmpty String username, @NotNull @NotEmpty String password);

    UserDetailResponse profile(String name);

    Page<UserDetailResponse> getAllUsers(Pageable pageable) throws AccessDeniedException;

    UserDetailResponse getUserById(Long id);

    void deleteUser(Long id);

    ResponseEntity<?> updatePassword(Long id, @Valid PasswordUpdateRequest request);

    ResponseEntity<ApiResponse<String>> updateUserRole(Long id, @Valid RoleUpdateRequest request);

}
