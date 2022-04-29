package com.kavka.apiservices.request;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Request to verify user credit application.")
public class CreditApplicationVerificationRequest {

    @NotNull(message = "Approved status cannot be null.")
    private Boolean isApproved;
    private String verificationNote;
    @Positive(message = "Credit line cannot be negative.")
    private Double creditLine;
}
