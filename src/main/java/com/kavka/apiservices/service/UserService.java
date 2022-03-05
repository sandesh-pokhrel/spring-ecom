package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.UserExistsException;
import com.kavka.apiservices.exception.UserNotFoundException;
import com.kavka.apiservices.model.Authority;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Objects;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getEnabledUserByEmail(String email) {
        return userRepository.findByEmailAndEnabled(email, true);
    }

    public User getByEmail(String email) {
        return userRepository
                .findByEmail(email).orElseThrow(() -> new UserNotFoundException("User " + email + " not found!"));
    }

    public int countByEmail(String email) {
        return userRepository.countByEmail(email);
    }

    public User saveUser(User user) {
        if (user == null) throw new IllegalArgumentException("User passed is not supported!");
        else if (countByEmail(user.getEmail()) > 0)
            throw new UserExistsException("Email address already exists!");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setIsVerified(false);
        if (Objects.isNull(user.getAuthorities())) {
            user.setAuthorities(Collections.singletonList(new Authority(null, "ROLE_USER", user)));
        }
        return userRepository.save(user);
    }
}
