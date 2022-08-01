package com.kavka.apiservices.repository;

import com.kavka.apiservices.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findAllByEnabledIsTrue();

    ProductCategory findByCode(String code);
}
