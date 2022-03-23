package com.kavka.apiservices.controller;

import com.kavka.apiservices.common.MailType;
import com.kavka.apiservices.dto.OrderDto;
import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Invoice;
import com.kavka.apiservices.model.Order;
import com.kavka.apiservices.model.OrderRequestMode;
import com.kavka.apiservices.request.OrderRequest;
import com.kavka.apiservices.service.InvoiceService;
import com.kavka.apiservices.service.OrderService;
import com.kavka.apiservices.util.MailUtil;
import com.lowagie.text.DocumentException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final RestTemplate restTemplate;
    private final MailUtil mailUtil;

    @Value("${orderdesk.api-url}")
    private String orderdeskUrl;

    @Autowired
    public OrderController(OrderService orderService,
                           InvoiceService invoiceService,
                           RestTemplate restTemplate,
                           MailUtil mailUtil) {
        this.orderService = orderService;
        this.invoiceService = invoiceService;
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
        Consumer<Order> fnInvoice = order1 -> {
            Invoice invoice = new Invoice(null, "PENDING", order1);
            invoiceService.save(invoice);
        };
        Map<String, Object> extras = new HashMap<>();
        extras.put("data", order);
        extras.put("callback", fnInvoice);
        mailUtil.sendMail(principal.getName(), MailType.INVOICE_MAIL, extras);
        return order;
    }

    @GetMapping("/send-to-orderdesk/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void sendOrderToOrderDesk(@PathVariable Integer orderId) {
        Order order = this.orderService.getById(orderId);
        OrderDto orderDto = this.orderService.buildRequest(order);
        ResponseEntity<Object> response =
                this.restTemplate.exchange(orderdeskUrl, HttpMethod.POST, new HttpEntity<>(orderDto), Object.class);

    }
}
