package com.lydiasystems.challenge.service;

import com.lydiasystems.challenge.entity.AppUser;
import com.lydiasystems.challenge.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserExists() {
        // Given
        AppUser appUser = new AppUser();
        appUser.setUsername("testuser");
        appUser.setPassword("testpassword");

        when(userRepository.findByUsername("testuser")).thenReturn(appUser);

        // When
        UserDetails userDetails = myUserDetailsService.loadUserByUsername("testuser");

        // Then
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
        assertEquals("testpassword", userDetails.getPassword());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        // Given
        when(userRepository.findByUsername("unknownuser")).thenReturn(null);

        // When / Then
        assertThrows(UsernameNotFoundException.class, () -> {
            myUserDetailsService.loadUserByUsername("unknownuser");
        });
        verify(userRepository, times(1)).findByUsername("unknownuser");
    }

    @Test
    public void testSaveUser_Success() {
        // Given
        String username = "newuser";
        String encodedPassword = "encodedpassword";
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(encodedPassword);

        // When
        myUserDetailsService.saveUser(username, encodedPassword);

        // Then
        verify(userRepository, times(1)).save(any(AppUser.class));
    }
}
