package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmailAndEnabled(String email, Boolean enabled);
    Optional<User> findByEmail(String email);
    int countByEmail(String email);
}
