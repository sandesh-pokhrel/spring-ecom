package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.exception.NotFoundException;
import com.kavka.apiservices.model.Billing;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.repository.BillingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillingRepository billingRepository;
    private final UserService userService;

    @Value("${generic.not.found}")
    private String notFound;

    @Value("${admin.not.configured}")
    private String adminNotConfigured;

    @Value("${mail.admin}")
    private String adminEmail;

    @Value("${admin.count.invalid}")
    private String adminCountInvalid;

    public Billing getById(Integer id) {
        return this.billingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(notFound, "Billing")));
    }

    public List<Billing> getAllByUser(Integer userId) {
        User user = this.userService.getById(userId);
        return this.billingRepository.findAllByUser(user);
    }

    public Billing getByIdAndEmail(Integer id, String email) {
        User user = this.userService.getByEmail(email);
        if (Objects.isNull(id))
            throw new InvalidOperationException(MessageFormat.format(notFound, "Customer Info"));
        return this.billingRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new InvalidOperationException(MessageFormat.format(notFound, "Customer Info")));
    }

    public Billing getByEmailAndIsDefault(String email, Boolean isDefault) {
        User user = this.userService.getByEmail(email);
        return this.billingRepository.findByUserAndIsDefault(user, isDefault)
                .orElseThrow(() -> new InvalidOperationException(MessageFormat.format(notFound, "Customer Info")));
    }

    public List<Billing> getAllByEmailAndIsDefault(String email, Boolean isDefault) {
        User user = this.userService.getByEmail(email);
        List<Billing> billings = this.billingRepository.findAllByUserAndIsDefault(user, isDefault);
        if (email.equals(adminEmail))
            if (billings.isEmpty())
                throw new RuntimeException(adminNotConfigured);
            else if (billings.size() > 1)
                throw new RuntimeException(adminCountInvalid);
        return billings;
    }

    public Billing saveBilling(Billing billing) {
        return this.billingRepository.save(billing);
    }

    public void delete(Billing billing) {
        this.billingRepository.delete(billing);
    }
}
