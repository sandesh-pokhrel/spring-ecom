package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Product;
import com.kavka.apiservices.model.ProductDetail;
import com.kavka.apiservices.repository.ProductDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final ProductService productService;

    public ProductDetail getById(Integer id) {
        return this.productDetailRepository.findById(id)
                .orElseThrow(() -> new InvalidOperationException("Product not found!"));
    }

    public List<ProductDetail> getAllByProduct(Integer productId) {
        Product product = this.productService.getById(productId);
        if (Objects.isNull(product))
            throw new InvalidOperationException("Product not found!");
        return this.productDetailRepository.findAllByProduct(product);
    }

    public ProductDetail save(ProductDetail productDetail) {
        return this.productDetailRepository.save(productDetail);
    }
}
