package com.kavka.apiservices.dto.mapper;

import com.kavka.apiservices.dto.OrderDto;
import com.kavka.apiservices.model.Customer;
import com.kavka.apiservices.request.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderRequestToDtoMapper {

    @Mapping(target = "shipping", ignore = true)
    @Mapping(target = "customer", source = "customer")
    @Mapping(target = "returnAddress", source = "returnAddress")
    OrderDto from(OrderRequest orderRequest, Customer customer, Customer returnAddress);
}
