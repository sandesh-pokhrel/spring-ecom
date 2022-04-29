package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.NotFoundException;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.model.UserStoreCredit;
import com.kavka.apiservices.repository.UserStoreCreditRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserStoreCreditService {

    private final UserStoreCreditRepository userStoreCreditRepository;

    public UserStoreCredit getByUser(User user) {
        return this.userStoreCreditRepository.findByUser(user).orElse(null);
    }

    public UserStoreCredit save(UserStoreCredit userStoreCredit) {
        return this.userStoreCreditRepository.save(userStoreCredit);
    }
}
