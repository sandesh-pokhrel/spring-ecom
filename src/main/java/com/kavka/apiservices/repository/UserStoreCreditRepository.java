package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.UserStoreCredit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStoreCreditRepository extends JpaRepository<UserStoreCredit, Integer> {
}
