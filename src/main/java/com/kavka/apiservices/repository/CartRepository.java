package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Cart;
import com.kavka.apiservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUser(User user);

    @Transactional
    @Modifying
    void deleteByUser(User user);
}
