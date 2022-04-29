package com.kavka.apiservices.model.mapper;

import com.kavka.apiservices.model.OrderPayment;
import com.kavka.apiservices.model.PaymentPlan;
import com.kavka.apiservices.request.OrderRequest;
import org.apache.commons.lang3.time.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Mapper(componentModel = "spring")
public interface PaymentRequestToModelMapper {

    @Mapping(target = "creditPaidDate", ignore = true)
    @Mapping(target = "creditDueDate", expression = "java(calculateDueDate(payment))")
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "id", ignore = true)
    OrderPayment from(OrderRequest.Payment payment);

    default Date calculateDueDate(OrderRequest.Payment payment) {
        if (payment.getPaymentPlan() == PaymentPlan.NET30)
            return DateUtils.addDays(new Date(), 30);
        else if (payment.getPaymentPlan() == PaymentPlan.NET60)
            return DateUtils.addDays(new Date(), 60);
        return null;
    }
}
