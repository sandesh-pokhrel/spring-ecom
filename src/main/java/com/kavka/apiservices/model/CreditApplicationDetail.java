package com.kavka.apiservices.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "credit_application_detail")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Store credit application from the user.")
public class CreditApplicationDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ein", unique = true, nullable = false)
    @Pattern(regexp = "[0-9]{9}", message = "EIN must constitute of 9 digits.")
    @NotNull(message = "EIN cannot be blank.")
    @JsonProperty("EIN")
    private String EIN;

    @Column(nullable = false)
    @Pattern(regexp = "[0-9]{5,15}", message = "Invalid phone number.")
    private String phoneNumber;

    @Column(nullable = false)
    @NotBlank(message = "{model.field.businessReference.validation.blank}")
    private String businessReferenceOne;
    @Column(nullable = false)
    @NotBlank(message = "{model.field.businessReference.validation.blank}")
    private String businessReferenceTwo;
    @Column(nullable = false)
    @NotBlank(message = "{model.field.businessReference.validation.blank}")
    private String businessReferenceThree;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CreditApplicationStatus creditApplicationStatus;

    private String verificationNote;

    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date appliedOn;

    @Temporal(TemporalType.DATE)
    private Date verifiedOn;

    @OneToOne
    @JoinColumn(name = "verified_by", referencedColumnName = "id", unique = true,
            foreignKey = @ForeignKey(name = "fk_credit_application_detail_verified_by"))
    private User verifiedBy;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true,
            foreignKey = @ForeignKey(name = "fk_credit_application_detail_user_id"))
    private User user;

    @JsonProperty("user")
    private void setUser(Integer userId) {
        this.user = new User();
        this.user.setId(userId);
    }
}
