package com.example.config.security;

import com.example.services.impl.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JWTService jwtService;

    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private FilterChain filterChain;

    private JwtFilter jwtFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        jwtFilter = new JwtFilter(jwtService, applicationContext);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_WithValidToken_SetsAuthentication() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String username = "testuser";
        String authHeader = "Bearer " + token;

        UserDetails userDetails = User.builder()
                .username(username)
                .password("password")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        request.addHeader("Authorization", authHeader);

        when(jwtService.extractUserName(token)).thenReturn(username);
        when(applicationContext.getBean(CustomUserDetailsService.class)).thenReturn(customUserDetailsService);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);

        jwtFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals(username, authentication.getName());
        assertTrue(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidToken_DoesNotSetAuthentication() throws ServletException, IOException {
        String token = "invalid-jwt-token";
        String username = "testuser";
        String authHeader = "Bearer " + token;

        UserDetails userDetails = User.builder()
                .username(username)
                .password("password")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        request.addHeader("Authorization", authHeader);

        when(jwtService.extractUserName(token)).thenReturn(username);
        when(applicationContext.getBean(CustomUserDetailsService.class)).thenReturn(customUserDetailsService);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(false);

        jwtFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithNoAuthorizationHeader_DoesNotSetAuthentication() throws ServletException, IOException {

        jwtFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void doFilterInternal_WithNonBearerToken_DoesNotSetAuthentication() throws ServletException, IOException {
        request.addHeader("Authorization", "Basic dGVzdDp0ZXN0");

        jwtFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    @Test
    void doFilterInternal_WithNullUsername_DoesNotSetAuthentication() throws ServletException, IOException {
        String token = "jwt-token-with-null-username";
        String authHeader = "Bearer " + token;

        request.addHeader("Authorization", authHeader);

        when(jwtService.extractUserName(token)).thenReturn(null);

        jwtFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUserName(token);
        verifyNoMoreInteractions(jwtService);
    }

    @Test
    void doFilterInternal_WithExistingAuthentication_DoesNotOverride() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String username = "testuser";
        String authHeader = "Bearer " + token;

        UserDetails existingUser = User.builder()
                .username("existinguser")
                .password("password")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_ADMIN")))
                .build();

        SecurityContextHolder.getContext().setAuthentication(
                new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        existingUser, null, existingUser.getAuthorities()));

        request.addHeader("Authorization", authHeader);

        when(jwtService.extractUserName(token)).thenReturn(username);

        jwtFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertEquals("existinguser", authentication.getName());
        assertTrue(authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));

        verify(filterChain).doFilter(request, response);
        verify(jwtService).extractUserName(token);
        verifyNoMoreInteractions(jwtService);
    }

    @Test
    void doFilterInternal_WithTokenExtractionException_ContinuesFilterChain() throws ServletException, IOException {
        String token = "malformed-jwt-token";
        String authHeader = "Bearer " + token;

        request.addHeader("Authorization", authHeader);

        when(jwtService.extractUserName(token)).thenThrow(new RuntimeException("Invalid token format"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithUserDetailsServiceException_ContinuesFilterChain() throws ServletException, IOException {
        String token = "valid-jwt-token";
        String username = "nonexistentuser";
        String authHeader = "Bearer " + token;

        request.addHeader("Authorization", authHeader);

        when(jwtService.extractUserName(token)).thenReturn(username);
        when(applicationContext.getBean(CustomUserDetailsService.class)).thenReturn(customUserDetailsService);
        when(customUserDetailsService.loadUserByUsername(username))
                .thenThrow(new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found"));

        jwtFilter.doFilterInternal(request, response, filterChain);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNull(authentication);

        verify(filterChain).doFilter(request, response);
    }
}