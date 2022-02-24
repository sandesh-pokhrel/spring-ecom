package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Customer;
import com.kavka.apiservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    List<Customer> findAllByUser(User user);

    List<Customer> findAllByUserAndIsDefault(User user, Boolean isDefault);

    Optional<Customer> findByIdAndUser(Integer id, User user);

    Optional<Customer> findByUserAndIsDefault(User user, Boolean isDefault);
}
