package com.kavka.apiservices.controller;

import com.kavka.apiservices.common.MailType;
import com.kavka.apiservices.dto.OrderDto;
import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.*;
import com.kavka.apiservices.request.OrderRequest;
import com.kavka.apiservices.response.OrderResponse;
import com.kavka.apiservices.service.InvoiceService;
import com.kavka.apiservices.service.OrderService;
import com.kavka.apiservices.service.UserService;
import com.kavka.apiservices.service.UserStoreCreditService;
import com.kavka.apiservices.util.MailUtil;
import com.lowagie.text.DocumentException;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Api(tags = "Order Controller",
        description = "Set of endpoints for order purpose.")
public class OrderController {

    private final OrderService orderService;
    private final InvoiceService invoiceService;
    private final UserStoreCreditService userStoreCreditService;
    private final UserService userService;
    private final RestTemplate restTemplate;
    private final MailUtil mailUtil;

    @Value("${orderdesk.api-url}")
    private String orderdeskUrl;

    @Value("${mail.admin}")
    private String adminEmail;

    @Value("${resource.unaccessible}")
    private String illegalResourceMessage;

    @GetMapping

    public List<Order> getAll() {
        return this.orderService.getAll();
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Integer id, Authentication authentication) {
        Order order = this.orderService.getById(id);
        if (!(authentication.getName().equals(adminEmail)
                || authentication.getName().equals(order.getUser().getEmail())))
            throw new InvalidOperationException(illegalResourceMessage);
        return order;
    }

    @GetMapping("/users/{id}")
    public List<Order> getAllByUser(@PathVariable Integer id) {
        return this.orderService.getAllByUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order saveOrder(@Valid @RequestBody OrderRequest orderRequest,
                           Authentication authentication) throws MessagingException, DocumentException {
        // TODO: custom request is disabled for now, enable later
        if (orderRequest.getOrderRequestMode() == OrderRequestMode.GUEST ||
                orderRequest.getOrderRequestMode() == OrderRequestMode.CUSTOM)
            throw new InvalidOperationException("Order request mode not supported");
        Order order = this.orderService.saveOrder(orderRequest, authentication);
        String name = authentication.getName();
        Consumer<Order> fnInvoice = order1 -> {
            Invoice invoice = Invoice.builder().order(order1).build();
            invoiceService.save(invoice);
        };
        // Order confirmation mail
        mailUtil.sendMail(name, MailType.INVOICE_MAIL, new HashMap<String, Object>() {{
            put("data", order);
            put("callback", fnInvoice);
        }});
        mailUtil.sendMail(adminEmail, MailType.INVOICE_MAIL, new HashMap<String, Object>() {{
            put("data", order);
        }});
        return order;
    }

    @GetMapping("/send-to-orderdesk/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse sendOrderToOrderDesk(@PathVariable Integer orderId) {
        Order order = this.orderService.getById(orderId);
        OrderDto orderDto = this.orderService.buildRequest(order);
        ResponseEntity<OrderResponse> response =
                this.restTemplate.exchange(orderdeskUrl, HttpMethod.POST, new HttpEntity<>(orderDto), OrderResponse.class);
        return response.getBody();
    }
}
