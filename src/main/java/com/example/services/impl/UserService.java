package com.example.services.impl;

import com.example.config.security.JWTService;
import com.example.dtos.UserDto;
import com.example.dtos.request.PasswordUpdateDto;
import com.example.dtos.request.RoleUpdateDto;
import com.example.dtos.request.UserRequestDto;
import com.example.dtos.response.ApiResponse;
import com.example.exceptions.domain.user.InvalidCredentialsException;
import com.example.exceptions.domain.user.UserAlreadyExistsException;
import com.example.exceptions.domain.user.UserNotFoundException;
import com.example.entities.User;
import com.example.repository.UserRepository;
import com.example.utils.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final JWTService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findUserByUsername(username);
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    public User getUserByName(String username) {
        return userRepository.findByUsername(username);
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return mapToUserDto(user);
    }

    public Set<UserDto> getAllUsers() throws AccessDeniedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUserRole = extractUserRole(auth);

        List<User> users = getUsersByRole(currentUserRole);
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toSet());
    }


    public Long register(UserRequestDto request) {
        if (userRepository.findByUsername(request.getUsername()) != null) {
            throw new UserAlreadyExistsException(request.getUsername());
        }
        User user = createNewUser(request);
        userRepository.save(user);
        log.info("User registered successfully: {}", request.getUsername());
        return user.getId();
    }

    public String authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            User user = userRepository.findByUsername(username);
            if (authentication.isAuthenticated()) {
                return jwtService.generateToken(username, user.getRole().name());
            }
            throw new InvalidCredentialsException();
        } catch (Exception e) {
            log.error("Authentication failed for user: {}", username, e);
            throw new InvalidCredentialsException();
        }
    }

    public UserDto profile(String username) {
        User user = findUserByUsername(username);
        return mapToUserDto(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        log.info("User deleted successfully with ID: {}", id);
    }

    public ResponseEntity<?> updatePassword(Long userId, PasswordUpdateDto passwordDto) {
        try {
            validatePasswordUpdate(passwordDto);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundException(userId));

            validateCurrentPassword(user, passwordDto.getCurrentPassword());
            validateNewPassword(user, passwordDto.getNewPassword());

            updateUserPassword(user, passwordDto.getNewPassword());
            log.info("Password updated successfully for user ID: {}", userId);

            return ResponseEntity.ok(ApiResponse.success("Password updated successfully"));
        } catch (UserNotFoundException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating password for user ID: {}", userId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Error updating password", e.getMessage() + " "));
        }
    }

    public ResponseEntity<ApiResponse<String>> updateUserRole(Long userId, RoleUpdateDto roleDto) {
        if (roleDto == null || roleDto.getRole() == null) {
            throw new IllegalArgumentException("Role information cannot be null");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        Role oldRole = user.getRole();
        if (oldRole.equals(roleDto.getRole())) {
            throw new IllegalArgumentException("User already has the specified role");
        }
        validateRoleUpdateAuthorization(roleDto.getRole());

        user.setRole(roleDto.getRole());
        userRepository.save(user);
        log.info("Role updated for user ID: {} from {} to {}", userId, oldRole, roleDto.getRole());


        return ResponseEntity.ok(ApiResponse.success("Role updated successfully"));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
               // .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    private String extractUserRole(Authentication auth) {
        return auth.getAuthorities().iterator().next().getAuthority();
    }

    private List<User> getUsersByRole(String currentUserRole) throws AccessDeniedException {
        return switch (currentUserRole) {
            case "ROLE_ADMIN" -> userRepository.findAll();
            case "ROLE_MANAGER" -> userRepository.findByRoleNot(Role.ADMIN);
            default -> throw new AccessDeniedException("Insufficient privileges");
        };
    }

    private User createNewUser(UserRequestDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.VIEWER);
        return user;
    }

    private void validatePasswordUpdate(PasswordUpdateDto passwordDto) {
        if (!passwordDto.getNewPassword().equals(passwordDto.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirmation do not match");
        }
    }

    private void validateCurrentPassword(User user, String currentPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
    }

    private void validateNewPassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("New password must be different from current password");
        }
    }

    private void updateUserPassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastModifiedDate(Instant.now());
        userRepository.save(user);
    }

    private void validateRoleAssignment(Role role) {
        if (role == Role.ADMIN) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                throw new IllegalArgumentException("Only admins can assign admin role");
            }
        }
    }

    private void validateRoleUpdateAuthorization(Role newRole) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isManager = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MANAGER"));
        if (!isAdmin && !isManager) {
            try {
                throw new AccessDeniedException("Insufficient privileges to update user roles");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }
        if (newRole == Role.ADMIN && !isAdmin) {
            try {
                throw new AccessDeniedException("Only admins can assign admin role");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}