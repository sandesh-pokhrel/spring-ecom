package com.kavka.apiservices.controller;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Cart;
import com.kavka.apiservices.model.CheckoutSummary;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.service.CartService;
import com.kavka.apiservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final UserService userService;
    private final CartService cartService;

    @Value("${pricing.tier-one.quantity}")
    private Integer tierOneQuantity;
    @Value("${pricing.tier-two.quantity}")
    private Integer tierTwoQuantity;
    @Value("${pricing.tier-three.quantity}")
    private Integer tierThreeQuantity;


    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CheckoutSummary checkout(Authentication authentication) {
        User user = this.userService.getByEmail(authentication.getName());
        Cart cart = this.cartService.getByUser(user.getId());
        if (Objects.isNull(cart) || Objects.isNull(cart.getCartItems()) || cart.getCartItems().size() < 1)
            throw new InvalidOperationException("Cart is empty!");
        CheckoutSummary checkoutSummary = CheckoutSummary.builder().cart(cart).build();
        cart.getCartItems().forEach(cartItem -> {
            double discountedPrice = 0D;
            checkoutSummary.setOriginalPrice(checkoutSummary.getOriginalPrice() + cartItem.getProductDetail().getPrice() * cartItem.getQuantity());

            if (cartItem.getQuantity() > tierThreeQuantity && Objects.nonNull(cartItem.getProductDetail().getTierThreePrice()))
                discountedPrice += cartItem.getQuantity() * cartItem.getProductDetail().getTierThreePrice();
            else if (cartItem.getQuantity() > tierTwoQuantity && Objects.nonNull(cartItem.getProductDetail().getTierTwoPrice()))
                discountedPrice += cartItem.getQuantity() * cartItem.getProductDetail().getTierTwoPrice();
            else if (cartItem.getQuantity() > tierOneQuantity && Objects.nonNull(cartItem.getProductDetail().getTierOnePrice()))
                discountedPrice += cartItem.getQuantity() * cartItem.getProductDetail().getTierOnePrice();

            checkoutSummary.setDiscountPrice(discountedPrice);
            checkoutSummary.setFinalPrice(checkoutSummary.getOriginalPrice() - checkoutSummary.getDiscountPrice());
        });
        return checkoutSummary;
    }
}
