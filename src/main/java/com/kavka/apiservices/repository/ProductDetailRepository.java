package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Product;
import com.kavka.apiservices.model.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {

    List<ProductDetail> findAllByProduct(Product product);
}
