package com.kavka.apiservices.validator;

import com.kavka.apiservices.model.PaymentType;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.model.UserStoreCredit;
import com.kavka.apiservices.request.OrderRequest;
import com.kavka.apiservices.service.OrderPaymentService;
import com.kavka.apiservices.service.UserService;
import com.kavka.apiservices.service.UserStoreCreditService;
import com.kavka.apiservices.validator.constraint.ValidOrderPayment;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class OrderPaymentMethodValidator
        implements ConstraintValidator<ValidOrderPayment, OrderRequest> {

    private final UserService userService;
    private final OrderPaymentService orderPaymentService;
    private final UserStoreCreditService userStoreCreditService;

    @Override
    public boolean isValid(OrderRequest orderRequest, ConstraintValidatorContext constraintValidatorContext) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if (orderRequest.getPayment().getPaymentType() == PaymentType.STORE_CREDIT) {
            User user = this.userService.getByEmail(email);
            UserStoreCredit storeCredit = this.userStoreCreditService.getByUser(user);
            if (Objects.isNull(storeCredit)) return false;
            if (!((storeCredit.getAvailableBalance() >= orderRequest.getTotalAmount())
                    && Objects.nonNull(orderRequest.getPayment().getPaymentPlan())))
                return false;
            return orderPaymentService.countByNotPaidCreditForUser(user) == 0;
        }
        return true;
    }
}
