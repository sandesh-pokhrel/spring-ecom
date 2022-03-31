package com.kavka.apiservices.dto.mapper;

import com.kavka.apiservices.dto.OrderDto;
import com.kavka.apiservices.model.Billing;
import com.kavka.apiservices.model.Order;
import com.kavka.apiservices.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderToDtoMapper {

    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "sourceId", ignore = true)
    @Mapping(target = "orderMetadata", expression = "java(new java.util.HashMap<String, String>(){{put(\"ship_notify_url\", \"link to your site for notification\");}})")
    @Mapping(target = "checkoutData", expression = "java(new java.util.HashMap<String, String>(){{put(\"PackagingSlip\", \"Link to packaging slip\");}})")
    @Mapping(target = "email", expression = "java(order.getUser().getEmail())")
    @Mapping(target = "shipping", source = "order.billing")
    @Mapping(target = "orderItems", source = "order.orderItems")
    OrderDto from(Order order, Billing customer, Billing returnAddress);

    @Mapping(target = "weight", ignore = true)
    @Mapping(target = "price", expression = "java(orderItem.getProductDetail().getPrice())")
    @Mapping(target = "name", expression = "java(orderItem.getProductDetail().getName())")
    @Mapping(target = "variationList", expression = "java(new java.util.HashMap<String, String>(){{put(\"Size\", \"Large\");}})")
    @Mapping(target = "metadata", expression = "java(new java.util.HashMap<String, String>(){{put(\"image\", \"preview thumbnail url\");}})")
    @Mapping(target = "code", expression = "java(buildCodeSku(orderItem))")
    OrderDto.OrderItem from(OrderItem orderItem);

    default String buildCodeSku(OrderItem orderItem) {
        String code = orderItem.getProductDetail().getProduct().getProductCategory().getCode();
        code += "-";
        code += orderItem.getProductDetail().getCode();
        code += "-";
        code += orderItem.getProductDetail().getProduct().getCode();
        return code;
    }
}
