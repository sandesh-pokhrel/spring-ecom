package com.kavka.apiservices.model.mapper;

import com.kavka.apiservices.model.Address;
import com.kavka.apiservices.model.Order;
import com.kavka.apiservices.model.OrderPayment;
import com.kavka.apiservices.model.OrderStatus;
import com.kavka.apiservices.request.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderRequestToModelMapper {

    @Mapping(target = "user", source = "billing.user")
    @Mapping(target = "shipping", source = "shipping")
    @Mapping(target = "billing", source = "billing")
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    Order from(OrderRequest orderRequest, Address billing, Address shipping,
               OrderPayment orderPayment, OrderStatus status);
}
