package com.kavka.apiservices.controller;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Billing;
import com.kavka.apiservices.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billings")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @Value("${resource.unaccessible}")
    private String illegalResourceMessage;

    @Value("${mail.admin}")
    private String adminEmail;

    @GetMapping("/{id}")
    public Billing getById(@PathVariable Integer id, Authentication authentication) {
        Billing billing = this.billingService.getById(id);
        if (!(authentication.getName().equals(adminEmail)
                || authentication.getName().equals(billing.getUser().getEmail())))
            throw new InvalidOperationException(illegalResourceMessage);
        return billing;
    }

    @GetMapping("/users/{id}")
    public List<Billing> getAllByUser(@PathVariable Integer id) {
        return this.billingService.getAllByUser(id);
    }

    @PostMapping
    public Billing save(@RequestBody Billing billing) {
        return this.billingService.saveBilling(billing);
    }
}
