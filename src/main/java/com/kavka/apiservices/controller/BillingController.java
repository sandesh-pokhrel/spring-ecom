package com.kavka.apiservices.controller;

import com.kavka.apiservices.model.Billing;
import com.kavka.apiservices.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/billings")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @GetMapping("/users/{id}")
    public List<Billing> getAllByUser(@PathVariable Integer id) {
        return this.billingService.getAllByUser(id);
    }

    @PostMapping
    public Billing save(@RequestBody Billing billing) {
        return this.billingService.saveBilling(billing);
    }
}
