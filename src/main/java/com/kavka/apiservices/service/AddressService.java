package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.exception.NotFoundException;
import com.kavka.apiservices.model.Address;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserService userService;

    @Value("${generic.not.found}")
    private String notFound;

    @Value("${admin.not.configured}")
    private String adminNotConfigured;

    @Value("${mail.admin}")
    private String adminEmail;

    @Value("${admin.count.invalid}")
    private String adminCountInvalid;

    public Address getById(Integer id) {
        return this.addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(notFound, "Address")));
    }

    public List<Address> getAllByUser(Integer userId) {
        User user = this.userService.getById(userId);
        return this.addressRepository.findAllByUser(user);
    }

    public Address getByIdAndEmail(Integer id, String email) {
        User user = this.userService.getByEmail(email);
        if (Objects.isNull(id))
            throw new InvalidOperationException(MessageFormat.format(notFound, "Address"));
        return this.addressRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new InvalidOperationException(MessageFormat.format(notFound, "Address")));
    }

    public Address getByEmailAndIsDefault(String email, Boolean isDefault) {
        User user = this.userService.getByEmail(email);
        return this.addressRepository.findByUserAndIsDefault(user, isDefault)
                .orElseThrow(() -> new InvalidOperationException(MessageFormat.format(notFound, "Address")));
    }

    public List<Address> getAllByEmailAndIsDefault(String email, Boolean isDefault) {
        User user = this.userService.getByEmail(email);
        List<Address> billings = this.addressRepository.findAllByUserAndIsDefault(user, isDefault);
        if (email.equals(adminEmail))
            if (billings.isEmpty())
                throw new RuntimeException(adminNotConfigured);
            else if (billings.size() > 1)
                throw new RuntimeException(adminCountInvalid);
        return billings;
    }

    public Address saveAddress(Address address) {
        return this.addressRepository.save(address);
    }

    public void delete(Address address) {
        this.addressRepository.delete(address);
    }
}
