package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Order;
import com.kavka.apiservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    List<Order> findAllByUser(User user);
}
