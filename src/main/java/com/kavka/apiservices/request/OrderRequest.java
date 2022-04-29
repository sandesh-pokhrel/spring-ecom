package com.kavka.apiservices.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kavka.apiservices.model.Billing;
import com.kavka.apiservices.model.OrderRequestMode;
import com.kavka.apiservices.model.PaymentPlan;
import com.kavka.apiservices.model.PaymentType;
import com.kavka.apiservices.validator.constraint.ValidOrderPayment;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidOrderPayment
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Holds information regarding user's request")
public class OrderRequest {

    @JsonProperty("order_request_mode")
    @NotNull(message = "Order request mode cannot be blank.")
    private OrderRequestMode orderRequestMode;
    @JsonProperty("billing_id")
    private Integer billingId;
    private Billing billing;
    @JsonProperty("source_id")
    private String sourceId;
    @JsonProperty("total_amount")
    private Double totalAmount;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_total")
    private Double shippingTotal;
    @JsonProperty("handling_total")
    private Double handlingTotal;
    @JsonProperty("tax_total")
    private Double taxTotal;
    @JsonProperty("checkout_data")
    private Map<String, String> checkoutData;
    @JsonProperty("order_metadata")
    private Map<String, String> orderMetadata;
    @JsonProperty("order_items")
    private List<OrderRequest.OrderItem> orderItems;
    private Payment payment;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItem {
        @JsonProperty("is_customized")
        private Boolean isCustomized;
        private Integer id;
        @JsonProperty("product_id")
        @NotNull(message = "Product must be present.")
        private Integer productId;
        @NotNull(message = "Quantity must be present.")
        private Integer quantity;
        @JsonProperty("variation_list")
        private Map<String, String> variationList;
        private Map<String, String> metadata;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payment {
        @JsonProperty("payment_type")
        @NotNull(message = "Payment type must be present.")
        private PaymentType paymentType;
        @JsonProperty("payment_plan")
        private PaymentPlan paymentPlan;
    }
}
