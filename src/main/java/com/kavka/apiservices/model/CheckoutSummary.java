package com.kavka.apiservices.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutSummary {

    private Cart cart;
    private double discountPrice;
    private double originalPrice;
    private double finalPrice;

}
