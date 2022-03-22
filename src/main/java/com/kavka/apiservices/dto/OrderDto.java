package com.kavka.apiservices.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kavka.apiservices.model.Billing;
import com.kavka.apiservices.model.OrderItem;
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
@ApiModel(description = "Holds the valid JSON structure for order to be passed to Orderdesk API")
public class OrderDto {

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
    private Billing shipping;
    private Billing billing;
    @JsonProperty("return_address")
    private Billing returnAddress;
    @JsonProperty("checkout_data")
    private Map<String, String> checkoutData;
    @JsonProperty("order_metadata")
    private Map<String, String> orderMetadata;
    @JsonProperty("order_items")
    private List<OrderItem> orderItems;
}
