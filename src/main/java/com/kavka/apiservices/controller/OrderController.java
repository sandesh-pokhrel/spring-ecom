package com.kavka.apiservices.controller;

import com.kavka.apiservices.common.MailType;
import com.kavka.apiservices.dto.OrderDto;
import com.kavka.apiservices.request.OrderRequest;
import com.kavka.apiservices.service.OrderService;
import com.kavka.apiservices.util.MailUtil;
import com.lowagie.text.DocumentException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import java.security.Principal;

@RestController
@RequestMapping("/orders")
@Api(tags = "Order Controller",
        description = "Set of endpoints for order purpose.")
public class OrderController {

    private final OrderService orderService;
    private final RestTemplate restTemplate;
    private final MailUtil mailUtil;

    @Value("${orderdesk.api-url}")
    private String orderdeskUrl;

    @Autowired
    public OrderController(OrderService orderService, RestTemplate restTemplate, MailUtil mailUtil) {
        this.orderService = orderService;
        this.restTemplate = restTemplate;
        this.mailUtil = mailUtil;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void postOrder(@RequestBody OrderRequest orderRequest,
                          Principal principal) throws MessagingException, DocumentException {
        OrderDto orderDto = this.orderService.buildRequest(orderRequest, principal);
        HttpEntity<OrderDto> httpEntity = new HttpEntity<>(orderDto);
        this.restTemplate.exchange(orderdeskUrl, HttpMethod.POST, httpEntity, String.class);
        mailUtil.sendMail(principal.getName(), MailType.INVOICE_MAIL, null, null);
    }
}
