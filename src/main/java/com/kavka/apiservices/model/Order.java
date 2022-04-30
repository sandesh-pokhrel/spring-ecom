package com.kavka.apiservices.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
@ApiModel(description = "Order related info.")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String shippingMethod;
    private Double totalAmount;
    private Double shippingTotal;
    private Double handlingTotal;
    private Double taxTotal;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date createdOn;

    @UpdateTimestamp
    @Temporal(TemporalType.DATE)
    private Date updatedOn;

    @OneToOne
    @JoinColumn(name = "billing_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_order_billing_id"))
    private Billing billing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_order_user_id"))
    @JsonBackReference
    private User user;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private OrderPayment orderPayment;
}
