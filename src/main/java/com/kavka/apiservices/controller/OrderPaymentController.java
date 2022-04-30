package com.kavka.apiservices.controller;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.OrderPayment;
import com.kavka.apiservices.model.PaymentType;
import com.kavka.apiservices.service.OrderPaymentService;
import com.kavka.apiservices.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/order-payments")
@RequiredArgsConstructor
public class OrderPaymentController {

    private final OrderPaymentService orderPaymentService;
    private final OrderService orderService;

    @Value("${store.credit.clearance.unapplicable}")
    private String paymentUnapplicable;


    @GetMapping("/pay/{order-id}")
    @ResponseStatus(HttpStatus.OK)
    public void payStoreCreditForOrder(@PathVariable("order-id") Integer orderId) {
        OrderPayment orderPayment = this.orderPaymentService.getByOrder(this.orderService.getById(orderId));
        if (orderPayment.getPaymentType() != PaymentType.STORE_CREDIT
                || Objects.nonNull(orderPayment.getCreditPaidDate())) {
            throw new InvalidOperationException(paymentUnapplicable);
        }
        this.orderPaymentService.payStoreCredit(orderId);
    }
}
