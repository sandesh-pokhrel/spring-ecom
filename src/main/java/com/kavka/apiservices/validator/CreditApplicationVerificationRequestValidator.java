package com.kavka.apiservices.validator;

import com.kavka.apiservices.request.CreditApplicationVerificationRequest;
import com.kavka.apiservices.validator.constraint.ValidCreditApplication;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class CreditApplicationVerificationRequestValidator
        implements ConstraintValidator<ValidCreditApplication, CreditApplicationVerificationRequest> {

    @Override
    public boolean isValid(CreditApplicationVerificationRequest verificationRequest,
                           ConstraintValidatorContext constraintValidatorContext) {
        return !(verificationRequest.getIsApproved() && Objects.isNull(verificationRequest.getCreditLine()));
    }
}
