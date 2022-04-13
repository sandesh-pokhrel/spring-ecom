package com.kavka.apiservices.controller;

import com.kavka.apiservices.common.MailType;
import com.kavka.apiservices.dto.OrderDto;
import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Invoice;
import com.kavka.apiservices.model.Order;
import com.kavka.apiservices.model.OrderRequestMode;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.request.OrderRequest;
import com.kavka.apiservices.response.OrderResponse;
import com.kavka.apiservices.service.InvoiceService;
import com.kavka.apiservices.service.OrderService;
import com.kavka.apiservices.service.UserService;
import com.kavka.apiservices.util.MailUtil;
import com.lowagie.text.DocumentException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@RestController
@RequestMapping("/orders")
@Api(tags = "Order Controller",
        description = "Set of endpoints for order purpose.")
public class OrderController {

    private final OrderService orderService;
    private final InvoiceService invoiceService;
    private final UserService userService;
    private final RestTemplate restTemplate;
    private final MailUtil mailUtil;

    @Value("${orderdesk.api-url}")
    private String orderdeskUrl;

    @Value("${mail.admin}")
    private String adminEmail;

    @Autowired
    public OrderController(OrderService orderService,
                           InvoiceService invoiceService,
                           UserService userService,
                           RestTemplate restTemplate,
                           MailUtil mailUtil) {
        this.orderService = orderService;
        this.invoiceService = invoiceService;
        this.restTemplate = restTemplate;
        this.userService = userService;
        this.mailUtil = mailUtil;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order saveOrder(@RequestBody OrderRequest orderRequest, Authentication authentication) throws MessagingException, DocumentException {
        if (orderRequest.getOrderRequestMode() == OrderRequestMode.GUEST ||
                orderRequest.getOrderRequestMode() == OrderRequestMode.CUSTOM)
            throw new InvalidOperationException("Billing method not supported");
        Order order = this.orderService.saveOrder(orderRequest, authentication);
        String name = authentication.getName();
        User user = userService.getByEmail(name);
        Consumer<Order> fnInvoice = order1 -> {
            Invoice invoice = new Invoice(null, order1);
            invoiceService.save(invoice);
        };
        Map<String, Object> extras = new HashMap<>();
        extras.put("data", order);
        extras.put("callback", fnInvoice);
        if (user.getIsVerified()) {
            mailUtil.sendMail(name, MailType.INVOICE_MAIL, extras);
            extras.remove("callback");
            mailUtil.sendMail(adminEmail, MailType.INVOICE_MAIL, extras);
        }

        if (!user.getIsVerified())
            mailUtil.sendMail(adminEmail, MailType.INVOICE_MAIL, extras);
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
