package com.kavka.apiservices.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_store_credit")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "User store credit information.")
public class UserStoreCredit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Double creditLine;
    private Double currentBalance;
    private Double availableBalance;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credit_application_detail_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_user_store_credit_cad_id"))
    @JsonBackReference
    private CreditApplicationDetail creditApplicationDetail;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_user_store_credit_user_id"))
    @JsonBackReference
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private User user;
}
