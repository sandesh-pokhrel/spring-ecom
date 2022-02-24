package com.kavka.apiservices.controller;

import com.kavka.apiservices.dto.OrderDto;
import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.OrderRequestMode;
import com.kavka.apiservices.request.OrderRequest;
import com.kavka.apiservices.service.OrderService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@RestController
@RequestMapping("/orders")
@Api(tags = "Order Controller",
        description = "Set of endpoints for order purpose.")
public class OrderController {

    private final OrderService orderService;
    private final RestTemplate restTemplate;

    @Value("${orderdesk.api-url}")
    private String orderdeskUrl;

    @Autowired
    public OrderController(OrderService orderService, RestTemplate restTemplate) {
        this.orderService = orderService;
        this.restTemplate = restTemplate;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void postOrder(@RequestBody OrderRequest orderRequest,
                          Principal principal) {
        if (orderRequest.getOrderRequestMode() != OrderRequestMode.GUEST && principal == null)
            throw new InvalidOperationException("Unauthorized user!");
        OrderDto orderDto = this.orderService.buildRequest(orderRequest, principal);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.set("ORDERDESK-STORE-ID", "random_store_id");
        httpHeaders.set("ORDERDESK-API-KEY", "456454354364565");
        HttpEntity<OrderDto> httpEntity = new HttpEntity<>(orderDto, httpHeaders);
        ResponseEntity<String> result = this.restTemplate.exchange(orderdeskUrl, HttpMethod.POST, httpEntity, String.class);
        System.out.println(result);
    }
}
