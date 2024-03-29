package com.kavka.apiservices.controller;

import com.kavka.apiservices.model.Cart;
import com.kavka.apiservices.model.CartItem;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.service.CartItemService;
import com.kavka.apiservices.service.CartService;
import com.kavka.apiservices.service.ProductDetailService;
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
    private final ProductDetailService productDetailService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartItem addToCart(@RequestBody CartItem cartItem, Authentication authentication) {
        User user = this.userService.getByEmail(authentication.getName());
        Cart cart = this.cartService.getByUser(user.getId());
        // check if product detail id is valid
        this.productDetailService.getById(cartItem.getProductDetail().getId());
        if (Objects.isNull(cart)) {
            cart = this.cartService.save(Cart.builder().user(user).build());
        }
        cartItem.setCart(cart);
        return this.cartItemService.save(cartItem);
    }

    @PostMapping("/quantity/change")
    @ResponseStatus(HttpStatus.CREATED)
    public CartItem changeQuantity(@RequestBody CartItem cartItem) {
        return this.cartItemService.changeQuantity(cartItem);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Integer id) {
        this.cartItemService.deleteById(id);
    }
}
