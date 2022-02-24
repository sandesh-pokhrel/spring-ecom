package com.kavka.apiservices.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kavka.apiservices.model.Customer;
import com.kavka.apiservices.model.OrderItem;
import com.kavka.apiservices.model.OrderRequestMode;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Holds information regarding user's request")
public class OrderRequest {

    @JsonProperty("order_request_mode")
    private OrderRequestMode orderRequestMode;
    @JsonProperty("customer_id")
    private Integer customerId;
    private Customer customer;
    @JsonProperty("source_id")
    private String sourceId;
    private String email;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_total")
    private Double shippingTotal;
    @JsonProperty("handling_total")
    private Double handlingTotal;
    @JsonProperty("tax_total")
    private Double taxTotal;
    @JsonProperty("date_added")
    private Date dateAdded;
    @JsonProperty("date_updated")
    private Date dateUpdated;
    @JsonProperty("checkout_data")
    private Map<String, String> checkoutData;
    @JsonProperty("order_metadata")
    private Map<String, String> orderMetadata;
    @JsonProperty("order_items")
    private List<OrderItem> orderItems;
}
