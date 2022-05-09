package com.kavka.apiservices.controller;

import com.kavka.apiservices.model.Product;
import com.kavka.apiservices.service.ProductService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Api(tags = "Product Controller",
        description = "Endpoints related to product.")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAll() {
        return this.productService.getAll();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Product getById(@PathVariable Integer id) {
        return this.productService.getById(id);
    }

    @GetMapping("/product-categories/{product-category-id}")
    @ResponseStatus(HttpStatus.OK)
    public List<Product> getAllByProductCategory(@PathVariable("product-category-id") Integer productCategoryId) {
        return this.productService.getAllByProductCategory(productCategoryId);
    }
}
