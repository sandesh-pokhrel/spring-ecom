package com.kavka.apiservices.service;

import com.kavka.apiservices.common.MailType;
import com.kavka.apiservices.exception.UserExistsException;
import com.kavka.apiservices.exception.UserNotFoundException;
import com.kavka.apiservices.model.Authority;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.repository.UserRepository;
import com.kavka.apiservices.util.MailUtil;
import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailUtil mailUtil;

    @Value("${role.admin}")
    private String adminRole;

    public boolean verifyIfCanAccessResource(Integer userId, Authentication authentication) {
        User user = getByEmail(authentication.getName());
        return Objects.equals(user.getId(), userId)
                || user.getAuthorities().stream().anyMatch(authority -> authority.getRole().equals(adminRole));
    }

    public User getEnabledUserByEmail(String email) {
        return userRepository.findByEmailAndEnabled(email, true);
    }

    public User getById(Integer id) {
        return userRepository
                .findById(id).orElseThrow(() -> new UserNotFoundException("User not found!"));
    }

    @Transactional
    public User getByEmail(String email) {
        return userRepository
                .findByEmail(email).orElseThrow(() -> new UserNotFoundException("User " + email + " not found!"));
    }

    public int countByEmail(String email) {
        return userRepository.countByEmail(email);
    }

    public User saveUser(User user) throws MessagingException, DocumentException {
        if (user == null) throw new IllegalArgumentException("User passed is not supported!");
        else if (countByEmail(user.getEmail()) > 0)
            throw new UserExistsException("Email address already exists!");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setIsVerified(false);
        if (Objects.isNull(user.getAuthorities())) {
            user.setAuthorities(Collections.singletonList(new Authority(null, "ROLE_USER", user)));
        }
        User savedUser = userRepository.save(user);
        mailUtil.sendMail(savedUser.getEmail(), MailType.USER_CREATION, null);
        return savedUser;
    }

    public User verifyUser(User user) throws MessagingException, DocumentException {
        User savedUser = userRepository.save(user);
        mailUtil.sendMail(savedUser.getEmail(), MailType.USER_VERIFICATION, null);
        return savedUser;
    }
}
