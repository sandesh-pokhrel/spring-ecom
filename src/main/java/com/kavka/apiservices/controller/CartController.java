package com.kavka.apiservices.controller;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Cart;
import com.kavka.apiservices.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @Value("${mail.admin}")
    private String adminEmail;

    @Value("${resource.unaccessible}")
    private String illegalResourceMessage;

    @GetMapping("/{id}")
    public Cart getById(@PathVariable Integer id, Authentication authentication) {
        Cart cart = this.cartService.getById(id);
        if (!(authentication.getName().equals(adminEmail)
                || authentication.getName().equals(cart.getUser().getEmail())))
            throw new InvalidOperationException(illegalResourceMessage);
        return cart;
    }

    @GetMapping("/users/{id}")
    public Cart getByUser(@PathVariable Integer id) {
        return this.cartService.getByUser(id);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteByUser(@PathVariable Integer id) {
        this.cartService.deleteByUser(id);
    }
}
