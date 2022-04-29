package com.kavka.apiservices.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "order_payment")
@ApiModel(description = "Order related info.")
public class OrderPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_order_payment_order_id"))
    @JsonBackReference
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Payment type cannot be null.")
    private PaymentType paymentType;

    @Enumerated(EnumType.STRING)
    private PaymentPlan paymentPlan;

    @Temporal(TemporalType.DATE)
    private Date creditDueDate;

    @Temporal(TemporalType.DATE)
    private Date creditPaidDate;


}
