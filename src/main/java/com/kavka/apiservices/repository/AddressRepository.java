package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Address;
import com.kavka.apiservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {

    List<Address> findAllByUser(User user);

    List<Address> findAllByUserAndIsDefault(User user, Boolean isDefault);

    Optional<Address> findByIdAndUser(Integer id, User user);

    Optional<Address> findByUserAndIsDefault(User user, Boolean isDefault);
}
