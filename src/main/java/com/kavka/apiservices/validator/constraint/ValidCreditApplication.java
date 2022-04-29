package com.kavka.apiservices.validator.constraint;

import com.kavka.apiservices.validator.CreditApplicationVerificationRequestValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CreditApplicationVerificationRequestValidator.class)
public @interface ValidCreditApplication {

    String message () default "Credit line cannot be null for approved application!";
    Class<?>[] groups () default {};
    Class<? extends Payload>[] payload () default {};
}
