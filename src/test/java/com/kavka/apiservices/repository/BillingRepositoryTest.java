package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Billing;
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
class BillingRepositoryTest {

    @Autowired
    private BillingRepository billingRepository;

    @Test
    void findAllByUserAndIsDefault() {
        User user = User.builder()
                .id(3)
                .build();
        List<Billing> billings = this.billingRepository.findAllByUserAndIsDefault(user, true);
        assertEquals(1, billings.size());
    }
}
