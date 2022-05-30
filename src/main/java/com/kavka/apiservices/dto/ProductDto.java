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
    private ProductCategory productCategory;
}
