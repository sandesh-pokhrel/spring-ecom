package com.kavka.apiservices.service;

import com.kavka.apiservices.dto.OrderDto;
import com.kavka.apiservices.dto.mapper.OrderToDtoMapper;
import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.*;
import com.kavka.apiservices.model.mapper.OrderRequestItemToModelMapper;
import com.kavka.apiservices.model.mapper.OrderRequestToModelMapper;
import com.kavka.apiservices.model.mapper.PaymentRequestToModelMapper;
import com.kavka.apiservices.repository.OrderRepository;
import com.kavka.apiservices.request.OrderRequest;
import com.kavka.apiservices.response.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BillingService billingService;
    private final UserService userService;
    private final ProductDetailService productDetailService;
    private final UserStoreCreditService userStoreCreditService;
    private final Validator validator;
    private final OrderToDtoMapper orderToDtoMapper;
    private final OrderRequestItemToModelMapper orderRequestItemToModelMapper;
    private final PaymentRequestToModelMapper paymentRequestToModelMapper;
    private final OrderRequestToModelMapper orderRequestToModelMapper;
    private final RestTemplate restTemplate;

    @Value("${mail.admin}")
    private String adminEmail;

    @Value("${orderdesk.api-url}")
    private String orderdeskUrl;


    protected void validateCustomer(@Valid Billing billing) {
        Set<ConstraintViolation<Billing>> violations = validator.validate(billing);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
    }

    protected List<OrderItem> buildOrderItemFromRequest(List<OrderRequest.OrderItem> orderItems) {
        return orderItems.stream().map(orderItem -> this.orderRequestItemToModelMapper
                .from(orderItem, productDetailService)).collect(Collectors.toList());
    }

    protected Billing getBilling(OrderRequest orderRequest, String name) {
        Billing billing;

        switch (orderRequest.getOrderRequestMode()) {
            case SPECIFIED:
                billing = this.billingService.getByIdAndEmail(orderRequest.getBillingId(), name);
                break;
            case DEFAULT:
                billing = this.billingService.getByEmailAndIsDefault(name, true);
                break;
            case CUSTOM:
            case GUEST:
                validateCustomer(orderRequest.getBilling());
                billing = orderRequest.getBilling();
                break;
            default:
                throw new InvalidOperationException("Order request mode is invalid!");
        }
        return billing;
    }

    private OrderResponse sendOrderToOrderDesk(@PathVariable Integer orderId) {
        Order order = this.getById(orderId);
        OrderDto orderDto = this.buildRequest(order);
        ResponseEntity<OrderResponse> response =
                this.restTemplate.exchange(orderdeskUrl, HttpMethod.POST, new HttpEntity<>(orderDto), OrderResponse.class);
        return response.getBody();
    }

    public List<Order> getAll() {
        return this.orderRepository.findAll();
    }

    public Order getById(Integer id) {
        return this.orderRepository.findById(id).orElseThrow(() -> new InvalidOperationException("Invalid order id!"));
    }

    public List<Order> getAllByUser(Integer userId) {
        User user = userService.getById(userId);
        return this.orderRepository.findAllByUser(user);
    }

    public Map<String, Object> saveOrder(OrderRequest orderRequest, Authentication authentication) {
        String name = authentication.getName();
        Billing billing = getBilling(orderRequest, name);
        User user = this.userService.getByEmail(name);
        List<OrderItem> orderItems = buildOrderItemFromRequest(orderRequest.getOrderItems());
        OrderPayment orderPayment = paymentRequestToModelMapper.from(orderRequest.getPayment());
        Order order = orderRequestToModelMapper.from(orderRequest, billing, orderPayment, OrderStatus.CONFIRMED);
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        orderPayment.setOrder(order);
        order.setOrderPayment(orderPayment);
        order.setOrderItems(orderItems);
        order.setUser(user);
        Order savedOrder = this.orderRepository.save(order);
        if (order.getOrderPayment().getPaymentType() == PaymentType.STORE_CREDIT)
            this.userStoreCreditService.updateBalances(savedOrder.getTotalAmount(), user);
        //OrderResponse orderResponse = sendOrderToOrderDesk(savedOrder.getId());  uncomment this in production
        OrderResponse orderResponse = null; // remove this in production
        return new HashMap<String, Object>(){{
            put("order", savedOrder);
            put("orderResponse", orderResponse);
        }};
    }

    public OrderDto buildRequest(Order order) {
        Billing kavkaCustomer = this.billingService.getAllByEmailAndIsDefault(adminEmail, true).get(0);
        Billing kavkaReturn = this.billingService.getAllByEmailAndIsDefault(adminEmail, false).get(0);

        return this.orderToDtoMapper.from(order, kavkaCustomer, kavkaReturn);
    }
}
