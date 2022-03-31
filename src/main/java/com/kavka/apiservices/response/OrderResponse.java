package com.kavka.apiservices.response;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderResponse {

    private String status;
    @JsonProperty("execution_time")
    private String executionTime;
    @JsonProperty("order")
    private OrderInfo orderInfo;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OrderInfo {
        private Long id;
        private String email;
        @JsonProperty("shipping_method")
        private String shippingMethod;
        @JsonProperty("quantity_total")
        private String quantityTotal;
        @JsonProperty("weight_total")
        private String weightTotal;
        @JsonProperty("product_total")
        private String productTotal;
        @JsonProperty("shipping_total")
        private String shippingTotal;
        @JsonProperty("handling_total")
        private String handlingTotal;
        @JsonProperty("tax_total")
        private Double taxTotal;
        @JsonProperty("discount_total")
        private Double discountTotal;
        @JsonProperty("order_total")
        private Double orderTotal;
        @JsonProperty("payment_status")
        private String paymentStatus;
        @JsonProperty("processor_balance")
        private Double processorBalance;
        @JsonProperty("refund_total")
        private Double refundTotal;
        @JsonProperty("source_name")
        private String sourceName;
        @JsonProperty("source_id")
        private String sourceId;
//        @Setter(AccessLevel.NONE)
//        Map<String, String> unmappedFields = new HashMap<>();
//
//        @JsonAnySetter
//        public void unmappedFields(String key, String value){
//            unmappedFields.put(key, value);
//        }
    }
}
