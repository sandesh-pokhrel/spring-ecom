package com.kavka.apiservices.controller;

import com.kavka.apiservices.common.MailType;
import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Order;
import com.kavka.apiservices.model.OrderRequestMode;
import com.kavka.apiservices.request.OrderRequest;
import com.kavka.apiservices.service.OrderService;
import com.kavka.apiservices.util.MailUtil;
import com.lowagie.text.DocumentException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

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
    public OrderController(OrderService orderService,
                           RestTemplate restTemplate,
                           MailUtil mailUtil) {
        this.orderService = orderService;
        this.restTemplate = restTemplate;
        this.mailUtil = mailUtil;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order saveOrder(@RequestBody OrderRequest orderRequest,
                          Principal principal) throws MessagingException, DocumentException {
        if (orderRequest.getOrderRequestMode() == OrderRequestMode.GUEST ||
                orderRequest.getOrderRequestMode() == OrderRequestMode.CUSTOM)
            throw new InvalidOperationException("Billing method not supported");
        Order order = this.orderService.saveOrder(orderRequest);
        Map<String, Object> extras = new HashMap<>();
        extras.put("order", order);
        mailUtil.sendMail(principal.getName(), MailType.INVOICE_MAIL, extras);
        return order;
    }

    @GetMapping("/send-to-orderdesk/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void sendOrderToOrderDesk() {
//        OrderDto orderDto = this.orderService.buildRequest(orderRequest, principal);
//        if (user.getIsVerified()) {
//            HttpEntity<OrderDto> httpEntity = new HttpEntity<>(orderDto);
//            this.restTemplate.exchange(orderdeskUrl, HttpMethod.POST, httpEntity, String.class);
//        }
    }
}
