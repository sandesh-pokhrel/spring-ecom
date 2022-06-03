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
@Table(name = "product")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Product related info.")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String code;
    @Column(length = 2000)
    private String description;
    private String imageUrl;
    private String artist;
    private String colorWayfair;
    private String countryOfManufacture;
    @Column(name = "feature_wayfair1")
    private String featureWayfairOne;
    @Column(name = "feature_wayfair2")
    private String featureWayfairTwo;
    @Column(name = "feature_wayfair3")
    private String featureWayfairThree;
    @Column(name = "feature_wayfair4")
    private String featureWayfairFour;
    @Column(name = "feature_wayfair5")
    private String featureWayfairFive;
    @Column(name = "feature_wayfair6")
    private String featureWayfairSix;
    private String holidayWayfair;
    private String shipTypeWayfair;
    private String kavkaCollection;
    private Double leadTimeHoursWayfair;
    private Double replacementTimeHoursWayfair;
    private String shopifyTags;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_product_product_category_id"))
    private ProductCategory productCategory;
}
