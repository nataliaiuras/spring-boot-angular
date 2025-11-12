package com.example.util.enums;

import lombok.Getter;

@Getter
public enum Role {
    ADMIN("ROLE_ADMIN", "Administrator"),
    MANAGER("ROLE_MANAGER", "Manager"),
    USER("ROLE_USER", "Regular User"),
    CUSTOMER("ROLE_CUSTOMER", "Customer"),
    EMPLOYEE("ROLE_EMPLOYEE", "Employee"),
    VIEWER("ROLE_VIEWER", "Read-only User");
    private final String authority;
    private final String displayName;

    Role(String authority, String displayName) {
        this.authority = authority;
        this.displayName = displayName;
    }

}
