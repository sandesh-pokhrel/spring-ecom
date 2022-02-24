package com.kavka.apiservices.service;

import com.kavka.apiservices.repository.UserRepository;
import com.kavka.apiservices.exception.UserNotFoundException;
import com.kavka.apiservices.model.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getEnabledUserByEmail(String email) {
        return userRepository.findByEmailAndEnabled(email, true);
    }

    public User getByEmail(String email) {
        return userRepository.findById(email).orElseThrow(() ->
                new UserNotFoundException("User " + email + " not found!")
        );
    }
}
