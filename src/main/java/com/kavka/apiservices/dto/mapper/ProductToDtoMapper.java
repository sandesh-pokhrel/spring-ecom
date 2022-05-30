package com.kavka.apiservices.dto.mapper;

import com.kavka.apiservices.dto.ProductDto;
import com.kavka.apiservices.model.Product;
import com.kavka.apiservices.model.ProductDetail;
import com.kavka.apiservices.service.ProductDetailService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface ProductToDtoMapper {

    @Mapping(target = "maxPrice", expression = "java(maxPrice(productDetailService, product.getId()))")
    @Mapping(target = "minPrice", expression = "java(minPrice(productDetailService, product.getId()))")
    ProductDto from(Product product, ProductDetailService productDetailService);

    default Double minPrice(ProductDetailService productDetailService, Integer productId) {
        List<ProductDetail> productDetails = productDetailService.getAllByProduct(productId);
        return productDetails.stream().map(ProductDetail::getPrice).min(Comparator.naturalOrder()).orElse(0D);
    }

    default Double maxPrice(ProductDetailService productDetailService, Integer productId) {
        List<ProductDetail> productDetails = productDetailService.getAllByProduct(productId);
        return productDetails.stream().map(ProductDetail::getPrice).max(Comparator.naturalOrder()).orElse(0D);
    }
}
