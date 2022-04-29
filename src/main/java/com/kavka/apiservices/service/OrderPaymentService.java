package com.kavka.apiservices.service;

import com.kavka.apiservices.model.OrderPayment;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.repository.OrderPaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderPaymentService {

    private final OrderPaymentRepository orderPaymentRepository;

    public int countByNotPaidCreditForUser(User user) {
        List<OrderPayment> orderPayments = this.orderPaymentRepository.findNotPaidCreditForUser(user);
        if (Objects.isNull(orderPayments)) return 0;
        return orderPayments.size();
    }
}
