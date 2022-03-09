package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.Product;
import com.kavka.apiservices.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAllByProductCategory(ProductCategory productCategory);
}
