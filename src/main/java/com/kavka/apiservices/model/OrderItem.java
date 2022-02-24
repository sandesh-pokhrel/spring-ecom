package com.kavka.apiservices.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Order item related info.")
public class OrderItem {

    private String name;
    private Double price;
    private Integer quantity;
    private Double weight;
    private String code;
    @JsonProperty("variation_list")
    private Map<String, String> variationList;
    private Map<String, String> metadata;
}
