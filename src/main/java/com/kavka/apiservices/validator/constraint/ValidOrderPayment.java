package com.kavka.apiservices.validator.constraint;

import com.kavka.apiservices.validator.OrderPaymentMethodValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = OrderPaymentMethodValidator.class)
public @interface ValidOrderPayment {

    String message() default "Store credit payment verification failed!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
