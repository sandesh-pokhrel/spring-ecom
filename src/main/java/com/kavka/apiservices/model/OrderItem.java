package com.kavka.apiservices.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_item")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Order item related info.")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private Double price;
    private Integer quantity;
    private Double weight;
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_order_item_order_id"))
    private Order order;
//    @JsonProperty("variation_list")
//    private Map<String, String> variationList;
//    private Map<String, String> metadata;
}
