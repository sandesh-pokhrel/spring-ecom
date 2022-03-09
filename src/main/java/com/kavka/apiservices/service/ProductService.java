package com.kavka.apiservices.service;

import com.kavka.apiservices.model.Product;
import com.kavka.apiservices.model.ProductCategory;
import com.kavka.apiservices.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryService productCategoryService;

    public Product getById(Integer id) {
        return this.productRepository.findById(id).orElse(null);
    }

    public List<Product> getAll() {
        return this.productRepository.findAll();
    }

    public List<Product> getAllByProductCategory(Integer productCategoryId) {
        ProductCategory productCategory = this.productCategoryService.getById(productCategoryId);
        return this.productRepository.findAllByProductCategory(productCategory);
    }
}
