package com.kavka.apiservices.controller;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Address;
import com.kavka.apiservices.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @Value("${resource.unaccessible}")
    private String illegalResourceMessage;

    @Value("${mail.admin}")
    private String adminEmail;

    private void throwIfIllegalUser(Address address, Authentication authentication) {

        if (!(authentication.getName().equals(adminEmail)
                || authentication.getName().equals(address.getUser().getEmail())))
            throw new InvalidOperationException(illegalResourceMessage);
    }

    @GetMapping("/{id}")
    public Address getById(@PathVariable Integer id, Authentication authentication) {
        Address address = this.addressService.getById(id);
        throwIfIllegalUser(address, authentication);
        return address;
    }

    @GetMapping("/users/{id}")
    public List<Address> getAllByUser(@PathVariable Integer id) {
        return this.addressService.getAllByUser(id);
    }

    @PostMapping
    public Address save(@RequestBody Address address) {
        return this.addressService.saveAddress(address);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, Authentication authentication) {
        Address address = this.addressService.getById(id);
        throwIfIllegalUser(address, authentication);
        this.addressService.delete(address);
    }
}
