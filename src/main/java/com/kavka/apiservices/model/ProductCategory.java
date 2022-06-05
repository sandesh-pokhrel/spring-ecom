package com.kavka.apiservices.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product_category", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code", name = "unq_product_category_code")
})
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Product category related.")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    @NotBlank(message = "{model.field.code.validation.blank}")
    @Size(min = 3, max = 8, message = "{model.field.code.validation.size}")
    private String code;
    private String thumbnailImageUrl;
    private Boolean enabled;
    private String description;
}
