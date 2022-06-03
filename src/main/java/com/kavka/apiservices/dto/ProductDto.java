package com.kavka.apiservices.dto;

import com.kavka.apiservices.model.ProductCategory;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Integer id;
    private String name;
    private String code;
    private String description;
    private String imageUrl;
    private Double minPrice;
    private Double maxPrice;
    private String artist;
    private String colorWayfair;
    private String countryOfManufacture;
    private String featureWayfairOne;
    private String featureWayfairTwo;
    private String featureWayfairThree;
    private String featureWayfairFour;
    private String featureWayfairFive;
    private String featureWayfairSix;
    private String holidayWayfair;
    private String shipTypeWayfair;
    private String kavkaCollection;
    private Double leadTimeHoursWayfair;
    private Double replacementTimeHoursWayfair;
    private String shopifyTags;
    private ProductCategory productCategory;
}
