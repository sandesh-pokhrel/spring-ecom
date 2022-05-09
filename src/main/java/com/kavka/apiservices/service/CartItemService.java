package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.NotFoundException;
import com.kavka.apiservices.model.Cart;
import com.kavka.apiservices.model.CartItem;
import com.kavka.apiservices.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartService cartService;

    @Value("${generic.not.found}")
    private String notFound;

    public CartItem getById(Integer id) {
        return this.cartItemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(MessageFormat.format(notFound, "Cart Item")));
    }

    public CartItem save(CartItem cartItem) {
        boolean productExists = false;
        Cart cart = this.cartService.getById(cartItem.getCart().getId());
        if (Objects.nonNull(cart.getCartItems()))
            for (CartItem cItem : cart.getCartItems()) {
                if (cItem.getProductDetail().equals(cartItem.getProductDetail())) {
                    productExists = true;
                    cartItem.setId(cItem.getId());
                    break;
                }
            }
        if (!productExists)
            return this.cartItemRepository.save(cartItem);
        else
            return this.changeQuantity(cartItem);
    }

    public CartItem changeQuantity(CartItem cartItem) {
        CartItem cItem = this.getById(cartItem.getId());
        cItem.setQuantity(cItem.getQuantity() + cartItem.getQuantity());
        return this.cartItemRepository.save(cItem);
    }

    public void deleteById(Integer id) {
        this.cartItemRepository.deleteById(id);
    }
}
