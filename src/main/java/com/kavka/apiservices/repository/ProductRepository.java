package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Product;
import com.kavka.apiservices.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByProductCategory(ProductCategory productCategory);

    Optional<Product> findByCode(String code);
}
