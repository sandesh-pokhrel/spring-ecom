package com.kavka.apiservices.model.mapper;

import com.kavka.apiservices.model.OrderPayment;
import com.kavka.apiservices.request.OrderRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface PaymentRequestToModelMapper {

    @Mapping(target = "order", ignore = true)
    @Mapping(target = "id", ignore = true)
    OrderPayment from(OrderRequest.Payment payment);
}
