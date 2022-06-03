package com.kavka.apiservices.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

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
    private String name;
    @Column(nullable = false)
    private String code;
    private String sku;
    private String ctSku;
    private Double price;
    private Double tierOnePrice;
    private Double tierTwoPrice;
    private Double tierThreePrice;
    private String ctPrintTemplate;
    private Double cartonDepth;
    private Double cartonHeight;
    private Double cartonWidth;
    private String mainImageUrl;
    @Column(name = "other_image1")
    private String otherImageOne;
    @Column(name = "other_image2")
    private String otherImageTwo;
    @Column(name = "other_image3")
    private String otherImageThree;
    @Column(name = "other_image4")
    private String otherImageFour;
    @Column(name = "other_image5")
    private String otherImageFive;
    @Column(name = "other_image6")
    private String otherImageSix;
    @Column(name = "other_image7")
    private String otherImageSeven;
    @Column(name = "other_image8")
    private String otherImageEight;
    private String printSize;
    private String productDimensions;
    private Double productMaxDepth;
    private Double productMaxHeight;
    private Double productMaxWidth;
    private Double productWeight;
    private Double shippingWeight;
    private String productTypeWayfair;
    private String seasonWayfair;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_product_product_id"))
    private Product product;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDetail that = (ProductDetail) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
