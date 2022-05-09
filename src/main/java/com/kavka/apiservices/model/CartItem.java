package com.kavka.apiservices.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_cart_item_cart_id"))
    @JsonBackReference
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_detail_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_cart_item_product_detail_id"))
    private ProductDetail productDetail;

    @JsonProperty("productDetailId")
    public void setProductDetail(Integer productDetailId) {
        productDetail = new ProductDetail();
        productDetail.setId(productDetailId);
    }
}
