package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Product;
import com.kavka.apiservices.model.ProductDetail;
import com.kavka.apiservices.repository.ProductDetailRepository;
import lombok.AllArgsConstructor;
import org.omg.CORBA.DynAnyPackage.Invalid;
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

    public List<ProductDetail> getAllByProductDetail(Integer productDetailId) {
        Product product = this.productService.getById(productDetailId);
        if (Objects.isNull(product))
            throw new InvalidOperationException("Product not found!");
        return this.productDetailRepository.findAllByProduct(product);
    }
}
