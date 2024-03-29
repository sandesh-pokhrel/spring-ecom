package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Address;
import com.kavka.apiservices.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Test
    void findAllByUserAndIsDefault() {
        User user = User.builder()
                .id(3)
                .build();
        List<Address> billings = this.addressRepository.findAllByUserAndIsDefault(user, true);
        assertEquals(1, billings.size());
    }
}
