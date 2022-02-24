package com.kavka.apiservices.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Customer related info.")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonProperty("first_name")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;
    @NotNull(message = "Last name cannot be blank")
    @JsonProperty("last_name")
    private String lastName;
    private String company;
    @JsonProperty("address1")
    private String addressOne;
    @JsonProperty("address2")
    private String addressTwo;
    private String city;
    private String state;
    @JsonProperty("postal_code")
    private String postalCode;
    private String country;
    private String phone;
    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "email", referencedColumnName = "email",
            foreignKey = @ForeignKey(name = "fk_customer_email"))
    @JsonBackReference
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id.equals(customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
