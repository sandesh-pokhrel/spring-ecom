package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.NotFoundException;
import com.kavka.apiservices.model.Cart;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;

    @Value("${generic.not.found}")
    private String notFound;

    public Cart getById(Integer id) {
        return this.cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(notFound, "Cart")));
    }

    public Cart getByUser(Integer userId) {
        User user = this.userService.getById(userId);
        return this.cartRepository.findByUser(user);
    }

    public Cart save(Cart cart) {
        return this.cartRepository.save(cart);
    }

    public void deleteByUser(Integer userId) {
        User user = this.userService.getById(userId);
        this.cartRepository.deleteByUser(user);
    }
}
