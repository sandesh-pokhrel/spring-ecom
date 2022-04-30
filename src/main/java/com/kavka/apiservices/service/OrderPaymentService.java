package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.exception.NotFoundException;
import com.kavka.apiservices.model.Order;
import com.kavka.apiservices.model.OrderPayment;
import com.kavka.apiservices.model.PaymentType;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.repository.OrderPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderPaymentService {

    private final OrderPaymentRepository orderPaymentRepository;
    private final OrderService orderService;
    private final UserStoreCreditService userStoreCreditService;

    public OrderPayment getByOrder(Order order) {
        return this.orderPaymentRepository.findByOrder(order)
                .orElseThrow(() -> new NotFoundException("Order payment not found for given order!"));
    }

    public int countByNotPaidCreditForUser(User user) {
        List<OrderPayment> orderPayments = this.orderPaymentRepository.findNotPaidCreditForUser(user);
        if (Objects.isNull(orderPayments)) return 0;
        return orderPayments.size();
    }

    public void payStoreCredit(Integer orderId) {
        Order order = this.orderService.getById(orderId);
        if (order.getOrderPayment().getPaymentType() != PaymentType.STORE_CREDIT)
            throw new InvalidOperationException("Payment not of type store credit.");
        OrderPayment orderPayment = this.getByOrder(order);
        orderPayment.setCreditPaidDate(new Date());
        this.userStoreCreditService.updateBalances(-order.getTotalAmount(), order.getUser());
        this.orderPaymentRepository.save(orderPayment);
    }
}
