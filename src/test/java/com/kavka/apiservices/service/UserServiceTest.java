package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.UserNotFoundException;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.repository.UserRepository;
import com.kavka.apiservices.util.MailUtil;
import com.lowagie.text.DocumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private MailUtil mailUtil;

    @BeforeEach
    void setup() {
        this.userService = new UserService(userRepository, passwordEncoder, mailUtil);
    }

    @Test
    void getByEmail() {
        when(this.userRepository.findByEmail(anyString()))
                .thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> this.userService.getByEmail(anyString()), "Exception thrown");
        verify(userRepository).findByEmail(anyString());
    }

    @Test
    void saveUser() throws MessagingException, DocumentException {
        User user = User.builder()
                .email("some@email.com")
                .firstName("first name")
                .lastName("last name")
                .build();

        User userReturned = User.builder()
                .id(1001)
                .email("some@email.com")
                .firstName("first name")
                .lastName("last name")
                .build();

        when(this.userRepository.countByEmail(anyString())).thenReturn(0);
        lenient().doReturn("RANDOM_ENCODED_STRING").when(passwordEncoder).encode(anyString());
        when(this.userRepository.save(any())).thenReturn(userReturned);
        doNothing().when(mailUtil).sendMail(anyString(), any(), any());

        assertEquals(1001, this.userService.saveUser(user).getId(), "Failed");
    }
}
