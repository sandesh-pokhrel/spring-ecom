package com.kavka.apiservices.controller;

import com.kavka.apiservices.model.ProductDetail;
import com.kavka.apiservices.service.ProductDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-details")
@RequiredArgsConstructor
public class ProductDetailController {

    private final ProductDetailService productDetailService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDetail getById(@PathVariable Integer id) {
        return this.productDetailService.getById(id);
    }

    @GetMapping("/products/{product-id}")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDetail> getByProduct(@PathVariable("product-id") Integer productId) {
        return this.productDetailService.getAllByProduct(productId);
    }
}
