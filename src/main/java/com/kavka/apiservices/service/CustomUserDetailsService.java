package com.kavka.apiservices.service;

import com.kavka.apiservices.model.CustomUserDetails;
import com.kavka.apiservices.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService usersService;

    public CustomUserDetailsService(UserService usersService) {
        this.usersService = usersService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.usersService.getEnabledUserByEmail(email);
        if (Objects.isNull(user)) throw new UsernameNotFoundException("User not found");
        return new CustomUserDetails(user);
    }
}
