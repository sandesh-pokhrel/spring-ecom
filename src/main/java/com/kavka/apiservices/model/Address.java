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
@Table(name = "address")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Customer related info.")
public class Address {
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
    @NotBlank(message = "Address one cannot be blank")
    private String addressOne;
    @JsonProperty("address2")
    private String addressTwo;
    @NotBlank(message = "City cannot be blank")
    private String city;
    private String state;
    @JsonProperty("postal_code")
    @NotBlank(message = "Postal code cannot be blank")
    private String postalCode;
    private String zip;
    @NotBlank(message = "Country cannot be blank")
    private String country;
    private String phone;
    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_customer_user_id"))
    @JsonBackReference
    private User user;

    @JsonProperty("userId")
    private void setUser(Integer userId) {
        this.user = new User();
        user.setId(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id.equals(address.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
