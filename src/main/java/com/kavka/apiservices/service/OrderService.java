package com.kavka.apiservices.service;

import com.kavka.apiservices.dto.OrderDto;
import com.kavka.apiservices.dto.mapper.OrderRequestToDtoMapper;
import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Customer;
import com.kavka.apiservices.request.OrderRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import java.security.Principal;
import java.util.Objects;
import java.util.Set;

@Service
public class OrderService {

    private final CustomerService customerService;
    private final Validator validator;
    private final OrderRequestToDtoMapper orderRequestToDtoMapper;

    @Value("${mail.admin}")
    private String adminEmail;

    public OrderService(CustomerService customerService,
                        Validator validator,
                        OrderRequestToDtoMapper orderRequestToDtoMapper) {
        this.customerService = customerService;
        this.validator = validator;
        this.orderRequestToDtoMapper = orderRequestToDtoMapper;
    }

    private void validateCustomer(@Valid Customer customer) {
        Set<ConstraintViolation<Customer>> violations = validator.validate(customer);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
    }

    public OrderDto buildRequest(OrderRequest orderRequest, Principal principal) {
        if (Objects.isNull(orderRequest) || Objects.isNull(orderRequest.getOrderRequestMode()))
            throw new InvalidOperationException("Order request mode is invalid!");
        Customer customer;
        Customer kavkaCustomer = this.customerService.getAllByEmailAndIsDefault(adminEmail, true).get(0);
        Customer kavkaReturn = this.customerService.getAllByEmailAndIsDefault(adminEmail, false).get(0);
        switch (orderRequest.getOrderRequestMode()) {
            case SPECIFIED:
                customer = this.customerService.getByIdAndEmail(orderRequest.getCustomerId(), principal.getName());
                break;
            case DEFAULT:
                customer = this.customerService.getByEmailAndIsDefault(principal.getName(), true);
                break;
            case CUSTOM:
            case GUEST:
                validateCustomer(orderRequest.getCustomer());
                customer = orderRequest.getCustomer();
                break;
            default:
                throw new InvalidOperationException("Order request mode is invalid!");
        }
        OrderDto orderDto = this.orderRequestToDtoMapper.from(orderRequest, kavkaCustomer, kavkaReturn);
        orderDto.setShipping(customer);
        return orderDto;
    }
}
