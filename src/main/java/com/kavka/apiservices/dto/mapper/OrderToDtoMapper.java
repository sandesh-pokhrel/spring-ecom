package com.kavka.apiservices.dto.mapper;

import com.kavka.apiservices.dto.OrderDto;
import com.kavka.apiservices.model.Billing;
import com.kavka.apiservices.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface OrderToDtoMapper {

    @Mapping(target = "sourceId", ignore = true)
    @Mapping(target = "orderMetadata", expression = "java(new java.util.HashMap<String, String>(){{put(\"ship_notify_url\", \"link to your site for notification\");}})")
    @Mapping(target = "checkoutData", expression = "java(new java.util.HashMap<String, String>(){{put(\"PackagingSlip\", \"Link to packaging slip\");}})")
    @Mapping(target = "email", expression = "java(order.getUser().getEmail())")
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "dateAdded", ignore = true)
    @Mapping(target = "shipping", ignore = true)
    OrderDto from(Order order, Billing billing, Billing returnAddress);
}
