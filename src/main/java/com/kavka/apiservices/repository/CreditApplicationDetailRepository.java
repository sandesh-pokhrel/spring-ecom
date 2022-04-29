package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.CreditApplicationDetail;
import com.kavka.apiservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditApplicationDetailRepository extends JpaRepository<CreditApplicationDetail, Integer> {

    Optional<CreditApplicationDetail> findByUser(User user);

    Optional<CreditApplicationDetail> findByEIN(String EIN);
}
