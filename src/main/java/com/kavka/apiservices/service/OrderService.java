package com.kavka.apiservices.service;

import com.kavka.apiservices.dto.OrderDto;
import com.kavka.apiservices.dto.mapper.OrderToDtoMapper;
import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.*;
import com.kavka.apiservices.model.mapper.OrderRequestItemToModelMapper;
import com.kavka.apiservices.repository.OrderRepository;
import com.kavka.apiservices.request.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
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
    private final OrderToDtoMapper orderToDtoMapper;
    private final OrderRequestItemToModelMapper orderRequestItemToModelMapper;

    @Value("${mail.admin}")
    private String adminEmail;

    public OrderService(OrderRepository orderRepository,
                        BillingService billingService,
                        UserService userService,
                        ProductDetailService productDetailService,
                        Validator validator,
                        OrderToDtoMapper orderToDtoMapper,
                        OrderRequestItemToModelMapper orderRequestItemToModelMapper) {
        this.orderRepository = orderRepository;
        this.billingService = billingService;
        this.userService = userService;
        this.productDetailService = productDetailService;
        this.validator = validator;
        this.orderToDtoMapper = orderToDtoMapper;
        this.orderRequestItemToModelMapper = orderRequestItemToModelMapper;
    }

    public boolean isResourceAccessible(Integer orderId, Authentication authentication) {
        Order order = this.getById(orderId);
        return  (authentication.getName().equals(adminEmail) || authentication.getName().equals(order.getUser().getEmail()));
    }

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

    public Order getById(Integer id) {
        return this.orderRepository.findById(id).orElseThrow(() -> new InvalidOperationException("Invalid order id!"));
    }

    public List<Order> getAllByUser(Integer userId) {
        User user = userService.getById(userId);
        return this.orderRepository.findAllByUser(user);
    }

    public Order saveOrder(OrderRequest orderRequest, Authentication authentication) {
        String name = authentication.getName();
        Billing billing = getBilling(orderRequest, name);
        User user = this.userService.getByEmail(name);
        List<OrderItem> orderItems = buildOrderItemFromRequest(orderRequest.getOrderItems());
        Order order = Order.builder()
                .id(null)
                .billing(billing)
                .handlingTotal(orderRequest.getHandlingTotal())
                .shippingTotal(orderRequest.getShippingTotal())
                .taxTotal(orderRequest.getTaxTotal())
                .shippingMethod(orderRequest.getShippingMethod())
                .user(user)
                .status(OrderStatus.PENDING)
                .build();
        orderItems.forEach(orderItem -> orderItem.setOrder(order));
        order.setOrderItems(orderItems);
        return this.orderRepository.save(order);
    }

    public OrderDto buildRequest(Order order) {
        Billing kavkaCustomer = this.billingService.getAllByEmailAndIsDefault(adminEmail, true).get(0);
        Billing kavkaReturn = this.billingService.getAllByEmailAndIsDefault(adminEmail, false).get(0);

        return this.orderToDtoMapper.from(order, kavkaCustomer, kavkaReturn);
    }
}
