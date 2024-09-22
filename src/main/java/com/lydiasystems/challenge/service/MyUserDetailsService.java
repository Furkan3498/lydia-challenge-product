package com.lydiasystems.challenge.service;

import com.lydiasystems.challenge.entity.AppUser;
import com.lydiasystems.challenge.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUsername(username);
        if (appUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), new ArrayList<>());
    }


    public void saveUser(String username, String encodedPassword) {
        AppUser appUser = new AppUser();
        appUser.setUsername(username);
        appUser.setPassword(encodedPassword);
        userRepository.save(appUser);
    }
}
