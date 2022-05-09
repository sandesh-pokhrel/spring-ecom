package com.kavka.apiservices.controller;

import com.kavka.apiservices.model.Cart;
import com.kavka.apiservices.model.CartItem;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.service.CartItemService;
import com.kavka.apiservices.service.CartService;
import com.kavka.apiservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;
    private final CartService cartService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartItem addToCart(@RequestBody CartItem cartItem, Authentication authentication) {
        User user = this.userService.getByEmail(authentication.getName());
        Cart cart = this.cartService.getByUser(user.getId());
        if (Objects.isNull(cart)) {
            cart = this.cartService.save(Cart.builder().user(user).build());
        }
        cartItem.setCart(cart);
        return this.cartItemService.save(cartItem);
    }

    @PostMapping("/increment")
    @ResponseStatus(HttpStatus.CREATED)
    public CartItem incrementQuantity(@RequestBody CartItem cartItem) {
        return this.cartItemService.changeQuantity(cartItem);
    }
}
