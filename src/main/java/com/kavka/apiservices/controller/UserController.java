package com.kavka.apiservices.controller;

import com.kavka.apiservices.exception.InvalidUserCredentialException;
import com.kavka.apiservices.filter.JwtUtil;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.request.LoginRequest;
import com.kavka.apiservices.service.CustomUserDetailsService;
import com.kavka.apiservices.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/users")
@Api(tags = "User Controller",
        description = "Set of endpoints for authentication, password change, reset etc. User JWT for this feature.")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final AuthenticationManager authenticationManager;

    @PostMapping(value = "/authenticate")
    @ApiOperation(value = "Uses JWT for authentication.")
    public ResponseEntity<Map<String, Object>> authenticate(
            @ApiParam("Login object contains email and password information.") @Valid @RequestBody LoginRequest loginRequest) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                    loginRequest.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new InvalidUserCredentialException("Invalid user credentials provided!");
        }
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginRequest.getEmail());
        String jwtToken = jwtUtil.generateToken(userDetails);
        Map<String, Object> userMap = new HashMap<>();
        User user = this.userService.getByEmail(loginRequest.getEmail());
        userMap.put("user", user);
        userMap.put("token", jwtToken);
        return ResponseEntity.ok().body(userMap);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Uses registration.")
    public User saveUser(@Valid @RequestBody User user) throws MessagingException {
        return userService.saveUser(user);
    }
}
