package com.kavka.apiservices.model;

import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoice")
@ApiModel(description = "Invoice related info.")
public class Invoice {

    @Id
    @GenericGenerator(name = "invoice_id_generator", strategy = "com.kavka.apiservices.model.generator.InvoiceIdGenerator")
    @GeneratedValue(generator = "invoice_id_generator")
    private String id;

    @OneToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_invoice_order_id"))
    private Order order;
}
