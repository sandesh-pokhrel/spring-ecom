package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.ProductCategory;
import com.kavka.apiservices.repository.ProductCategoryRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductCategory> getAll() {
        return this.productCategoryRepository.findAll();
    }

    public List<ProductCategory> getAllEnabled() {
        return this.productCategoryRepository.findAllByEnabledIsTrue();
    }

    public ProductCategory getById(Integer id) {
        return this.productCategoryRepository.findById(id)
                .orElseThrow(() -> new InvalidOperationException("Product category not found!"));
    }

    public ProductCategory getByCode(String code) {
        return this.productCategoryRepository.findByCode(code);
    }

    public ProductCategory save(ProductCategory productCategory) {
        if (Objects.isNull(productCategory.getId()) && Objects.isNull(productCategory.getEnabled()))
            productCategory.setEnabled(true);
        return this.productCategoryRepository.save(productCategory);
    }
}
