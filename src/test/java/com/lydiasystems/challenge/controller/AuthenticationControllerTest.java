package com.lydiasystems.challenge.controller;

import com.lydiasystems.challenge.security.AuthenticationRequest;
import com.lydiasystems.challenge.security.JwtUtil;
import com.lydiasystems.challenge.service.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        // Given
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("testUser");  // Set username properly
        request.setPassword("testPassword");  // Set password properly
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // When
        ResponseEntity<?> response = authenticationController.registerUser(request);

        // Then
        verify(userDetailsService, times(1)).saveUser("testUser", "encodedPassword");  // Ensure the correct username is passed
        assertEquals(ResponseEntity.ok("User registered successfully!"), response);
    }



    @Test
    public void testRegisterUser_Failure() {
        // Given
        AuthenticationRequest request = new AuthenticationRequest("testUser", "testPassword");
        when(passwordEncoder.encode(request.getPassword())).thenThrow(new RuntimeException("Encode error"));

        // When
        ResponseEntity<?> response = authenticationController.registerUser(request);

        // Then
        verify(userDetailsService, never()).saveUser(anyString(), anyString());
        assertEquals("Error: Encode error", response.getBody());
    }

    // Add other tests such as createAuthenticationToken_Success etc.
}
