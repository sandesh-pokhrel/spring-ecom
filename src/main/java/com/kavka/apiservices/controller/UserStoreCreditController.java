package com.kavka.apiservices.controller;

import com.kavka.apiservices.model.User;
import com.kavka.apiservices.model.UserStoreCredit;
import com.kavka.apiservices.service.UserService;
import com.kavka.apiservices.service.UserStoreCreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-store-credits")
@RequiredArgsConstructor
public class UserStoreCreditController {

    private final UserStoreCreditService userStoreCreditService;
    private final UserService userService;

    @GetMapping("/users/{id}")
    public UserStoreCredit getByUser(@PathVariable Integer id) {
        User currentUser = this.userService.getById(id);
        return this.userStoreCreditService.getByUser(currentUser);
    }
}
