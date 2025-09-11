
package com.example.config.security;

import com.example.services.impl.CustomUserDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class JwtFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JWTService jwtService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void testFilterWithValidToken() throws Exception {
        String token = "valid-token";
        String username = "testuser";

        UserDetails userDetails = User.builder()
                .username(username)
                .password("password")
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        when(jwtService.extractUserName(token)).thenReturn(username);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.validateToken(token, userDetails)).thenReturn(true);

        mockMvc.perform(get("/api/banks")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void testFilterWithoutToken() throws Exception {
        mockMvc.perform(get("/api/banks"))
                .andExpect(status().isUnauthorized());
    }
}