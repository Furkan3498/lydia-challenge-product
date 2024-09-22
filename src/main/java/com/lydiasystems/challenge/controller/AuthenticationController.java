package com.lydiasystems.challenge.controller;

import com.lydiasystems.challenge.security.AuthenticationRequest;
import com.lydiasystems.challenge.security.AuthenticationResponse;
import com.lydiasystems.challenge.security.JwtUtil;
import com.lydiasystems.challenge.service.MyUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;

    private final MyUserDetailsService userDetailsService;

    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, MyUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthenticationRequest request) {
        try {
            // Encode the password before saving the user
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            userDetailsService.saveUser(request.getUsername(), encodedPassword);
            return ResponseEntity.ok("User registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Authenticate a user and generate a JWT token
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            // Attempt authentication with the provided credentials
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new Exception("Incorrect username or password", e);
        }

        // If authentication is successful, generate a JWT token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        // Return the JWT token in the response
        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }
}