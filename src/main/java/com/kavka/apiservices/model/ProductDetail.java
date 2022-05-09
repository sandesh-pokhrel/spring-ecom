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
@Table(name = "product_detail")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Product detail related info.")
public class ProductDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String code;
    private String colors;
    private Double price;
    private String imageUrl;
    private Double tierOnePrice;
    private Double tierTwoPrice;
    private Double tierThreePrice;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_product_product_id"))
    private Product product;
}
