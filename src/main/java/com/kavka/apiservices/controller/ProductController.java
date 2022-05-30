package com.kavka.apiservices.controller;

import com.kavka.apiservices.dto.ProductDto;
import com.kavka.apiservices.dto.mapper.ProductToDtoMapper;
import com.kavka.apiservices.model.Product;
import com.kavka.apiservices.model.ProductDetail;
import com.kavka.apiservices.service.ProductDetailService;
import com.kavka.apiservices.service.ProductService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@Api(tags = "Product Controller",
        description = "Endpoints related to product.")
public class ProductController {

    private final ProductService productService;
    private final ProductDetailService productDetailService;
    private final ProductToDtoMapper productToDtoMapper;

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
    public List<ProductDto> getAllByProductCategory(@PathVariable("product-category-id") Integer productCategoryId) {
        List<Product> products = this.productService.getAllByProductCategory(productCategoryId);
        return products.stream().map(product -> productToDtoMapper.from(product, productDetailService)).collect(Collectors.toList());
    }
}
