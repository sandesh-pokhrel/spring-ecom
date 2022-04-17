package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Billing;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BillingService {

    private final BillingRepository billingRepository;
    private final UserService userService;

    @Value("${mail.admin}")
    private String adminEmail;

    @Autowired
    public BillingService(BillingRepository billingRepository, UserService userService) {
        this.billingRepository = billingRepository;
        this.userService = userService;
    }

    public List<Billing> getAllByUser(Integer userId) {
        User user = this.userService.getById(userId);
        return this.billingRepository.findAllByUser(user);
    }

    public Billing getByIdAndEmail(Integer id, String email) {
        User user = this.userService.getByEmail(email);
        if (Objects.isNull(id)) throw new InvalidOperationException("Customer info not found!");
        return this.billingRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new InvalidOperationException("Customer info not found!"));
    }

    public Billing getByEmailAndIsDefault(String email, Boolean isDefault) {
        User user = this.userService.getByEmail(email);
        return this.billingRepository.findByUserAndIsDefault(user, isDefault)
                .orElseThrow(() -> new InvalidOperationException("Customer info not found!"));
    }

    public List<Billing> getAllByEmailAndIsDefault(String email, Boolean isDefault) {
        User user = this.userService.getByEmail(email);
        List<Billing> billings = this.billingRepository.findAllByUserAndIsDefault(user, isDefault);
        if (email.equals(adminEmail))
            if (billings.isEmpty())
                throw new RuntimeException("Kavka admin customer is not configured!");
            else if (billings.size() > 1)
                throw new RuntimeException("Kavka admin customer count should not exceed one!");
        return billings;
    }

    public Billing saveBilling(Billing billing) {
        return this.billingRepository.save(billing);
    }
}
