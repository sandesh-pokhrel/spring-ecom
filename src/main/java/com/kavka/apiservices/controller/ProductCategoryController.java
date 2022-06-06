package com.kavka.apiservices.controller;

import com.kavka.apiservices.model.ProductCategory;
import com.kavka.apiservices.service.ProductCategoryService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/product-categories")
@AllArgsConstructor
@Api(tags = "Product categories Controller",
        description = "Endpoints related to product categories.")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductCategory> getAll() {
        return this.productCategoryService.getAll();
    }

    @GetMapping("/enabled")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductCategory> getAllEnabled() {
        return this.productCategoryService.getAllEnabled();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Cacheable(cacheNames = "product-categories", key = "#id")
    public ProductCategory getById(@PathVariable Integer id) {
        return this.productCategoryService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCategory save(@Valid @RequestBody ProductCategory productCategory) {
        return this.productCategoryService.save(productCategory);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @CachePut(cacheNames = "product-categories", key = "#id")
    public ProductCategory update(@Valid @RequestBody ProductCategory productCategory,
                                  @PathVariable Integer id) {
        this.productCategoryService.getById(id);
        return this.productCategoryService.save(productCategory);
    }
}
