package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.CartItem;
import com.kavka.apiservices.model.ProductDetail;
import com.kavka.apiservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    @Query("select ci from CartItem ci inner join ci.cart c " +
            "where c.user = ?2 and ci.productDetail = ?1")
    Optional<CartItem> findByProductDetailAndUser(ProductDetail productDetail, User user);
}
