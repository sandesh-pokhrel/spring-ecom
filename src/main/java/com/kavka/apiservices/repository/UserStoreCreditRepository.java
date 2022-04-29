package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.User;
import com.kavka.apiservices.model.UserStoreCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserStoreCreditRepository extends JpaRepository<UserStoreCredit, Integer> {

    Optional<UserStoreCredit> findByUser(User user);
}
