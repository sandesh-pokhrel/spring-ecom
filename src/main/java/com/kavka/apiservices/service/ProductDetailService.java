package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.ProductDetail;
import com.kavka.apiservices.repository.ProductDetailRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;

    public ProductDetail getById(Integer id) {
        return this.productDetailRepository.findById(id)
                .orElseThrow(() -> new InvalidOperationException("Product not found"));
    }
}
