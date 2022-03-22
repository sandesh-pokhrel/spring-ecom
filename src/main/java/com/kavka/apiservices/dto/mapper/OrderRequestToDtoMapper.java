package com.kavka.apiservices.dto.mapper;

import com.kavka.apiservices.dto.OrderDto;
import com.kavka.apiservices.model.Billing;
import com.kavka.apiservices.request.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface OrderRequestToDtoMapper {

    @Mapping(target = "email", ignore = true)
    @Mapping(target = "dateUpdated", ignore = true)
    @Mapping(target = "dateAdded", ignore = true)
    @Mapping(target = "shipping", ignore = true)
    OrderDto from(OrderRequest orderRequest, Billing billing, Billing returnAddress);
}
