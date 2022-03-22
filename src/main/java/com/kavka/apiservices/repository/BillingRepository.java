package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Billing;
import com.kavka.apiservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Integer> {

    List<Billing> findAllByUser(User user);

    List<Billing> findAllByUserAndIsDefault(User user, Boolean isDefault);

    Optional<Billing> findByIdAndUser(Integer id, User user);

    Optional<Billing> findByUserAndIsDefault(User user, Boolean isDefault);
}
