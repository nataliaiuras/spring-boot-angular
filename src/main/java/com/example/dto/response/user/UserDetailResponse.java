package com.example.dto.response.user;


import com.example.dto.response.customer.CustomerResponse;
import com.example.util.enums.Role;

public record UserDetailResponse(Long id, String username, Role role, boolean enabled, CustomerResponse customer,
                                 String createdDate, String lastModifiedDate, Long version) {
}