package com.kavka.apiservices.service;

import com.kavka.apiservices.dto.mapper.OrderRequestToDtoMapper;
import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.*;
import com.kavka.apiservices.model.mapper.OrderRequestItemToModelMapper;
import com.kavka.apiservices.repository.OrderRepository;
import com.kavka.apiservices.request.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final BillingService billingService;
    private final UserService userService;
    private final ProductDetailService productDetailService;
    private final Validator validator;
    private final OrderRequestToDtoMapper orderRequestToDtoMapper;
    private final OrderRequestItemToModelMapper orderRequestItemToModelMapper;

    @Value("${mail.admin}")
    private String adminEmail;

    public OrderService(OrderRepository orderRepository,
                        BillingService billingService,
                        UserService userService,
                        ProductDetailService productDetailService,
                        Validator validator,
                        OrderRequestToDtoMapper orderRequestToDtoMapper,
                        OrderRequestItemToModelMapper orderRequestItemToModelMapper) {
        this.orderRepository = orderRepository;
        this.billingService = billingService;
        this.userService = userService;
        this.productDetailService = productDetailService;
        this.validator = validator;
        this.orderRequestToDtoMapper = orderRequestToDtoMapper;
        this.orderRequestItemToModelMapper = orderRequestItemToModelMapper;
    }

    private void validateCustomer(@Valid Billing billing) {
        Set<ConstraintViolation<Billing>> violations = validator.validate(billing);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
    }

    private List<OrderItem> buildOrderItemFromRequest(List<OrderRequest.OrderItem> orderItems) {
        return orderItems.stream().map(orderItem -> this.orderRequestItemToModelMapper
                .from(orderItem, productDetailService)).collect(Collectors.toList());
    }

    private Billing getBilling(OrderRequest orderRequest, Principal principal) {
        Billing billing;

        switch (orderRequest.getOrderRequestMode()) {
            case SPECIFIED:
                billing = this.billingService.getByIdAndEmail(orderRequest.getBillingId(), principal.getName());
                break;
            case DEFAULT:
                billing = this.billingService.getByEmailAndIsDefault(principal.getName(), true);
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

    public Order saveOrder(OrderRequest orderRequest) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        Billing billing = getBilling(orderRequest, principal);
        User user = this.userService.getByEmail(principal.getName());
        List<OrderItem> orderItems = buildOrderItemFromRequest(orderRequest.getOrderItems());
        Order order = Order.builder()
                .id(null)
                .billing(billing)
                .handlingTotal(orderRequest.getHandlingTotal())
                .shippingTotal(orderRequest.getShippingTotal())
                .taxTotal(orderRequest.getTaxTotal())
                .shippingMethod(orderRequest.getShippingMethod())
                .user(user)
                .orderItems(orderItems)
                .orderStatus(OrderStatus.PENDING)
                .build();
        return this.orderRepository.save(order);
    }

//    public OrderDto buildRequest(OrderRequest orderRequest, Principal principal) {
//        if (Objects.isNull(orderRequest) || Objects.isNull(orderRequest.getOrderRequestMode()))
//            throw new InvalidOperationException("Order request mode is invalid!");
//        Billing billing;
//        Billing kavkaCustomer = this.billingService.getAllByEmailAndIsDefault(adminEmail, true).get(0);
//        Billing kavkaReturn = this.billingService.getAllByEmailAndIsDefault(adminEmail, false).get(0);
//        switch (orderRequest.getOrderRequestMode()) {
//            case SPECIFIED:
//                billing = this.billingService.getByIdAndEmail(orderRequest.getCustomerId(), principal.getName());
//                break;
//            case DEFAULT:
//                billing = this.billingService.getByEmailAndIsDefault(principal.getName(), true);
//                break;
//            case CUSTOM:
//            case GUEST:
//                validateCustomer(orderRequest.getBilling());
//                billing = orderRequest.getBilling();
//                break;
//            default:
//                throw new InvalidOperationException("Order request mode is invalid!");
//        }
//        OrderDto orderDto = this.orderRequestToDtoMapper.from(orderRequest, kavkaCustomer, kavkaReturn);
//        orderDto.setShipping(billing);
//        return orderDto;
//    }
}
