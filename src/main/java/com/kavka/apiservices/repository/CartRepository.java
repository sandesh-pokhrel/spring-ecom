package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Cart;
import com.kavka.apiservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Cart findByUser(User user);
    void deleteByUser(User user);
}
