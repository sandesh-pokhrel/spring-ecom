package com.kavka.apiservices.model.mapper;

import com.kavka.apiservices.model.OrderItem;
import com.kavka.apiservices.request.OrderRequest;
import com.kavka.apiservices.service.ProductDetailService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderRequestItemToModelMapper {

    @Mapping(target = "productDetail", expression = "java(productDetailService.getById(orderItem.getProductId()))")
    @Mapping(target = "order", ignore = true)
    OrderItem from(OrderRequest.OrderItem orderItem, ProductDetailService productDetailService);
}
