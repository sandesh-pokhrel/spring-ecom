package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Customer;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserService userService;

    @Value("${mail.admin}")
    private String adminEmail;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, UserService userService) {
        this.customerRepository = customerRepository;
        this.userService = userService;
    }

    public Customer getByIdAndEmail(Integer id, String email) {
        User user = this.userService.getByEmail(email);
        if (Objects.isNull(id)) throw new InvalidOperationException("Customer info not found!");
        return this.customerRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new InvalidOperationException("Customer info not found!"));
    }

    public Customer getByEmailAndIsDefault(String email, Boolean isDefault) {
        User user = this.userService.getByEmail(email);
        return this.customerRepository.findByUserAndIsDefault(user, isDefault)
                .orElseThrow(() -> new InvalidOperationException("Customer info not found!"));
    }

    public List<Customer> getAllByEmailAndIsDefault(String email, Boolean isDefault) {
        User user = this.userService.getByEmail(email);
        List<Customer> customers = this.customerRepository.findAllByUserAndIsDefault(user, isDefault);
        if (email.equals(adminEmail))
            if (customers.isEmpty())
                throw new RuntimeException("Kavka admin customer is not configured!");
            else if (customers.size() > 1)
                throw new RuntimeException("Kavka admin customer count should not exceed one!");
        return customers;
    }
}
