package com.kavka.apiservices.aspect;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.model.Address;
import com.kavka.apiservices.model.CreditApplicationDetail;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class ResourceAccessibleAspect {

    private final UserService userService;

    @Value("${resource.unaccessible}")
    private String illegalResourceMessage;

    @Value("${user.save.illegal}")
    private String illegalSave;

    @Value("${mail.admin}")
    private String adminEmail;

    @Pointcut("execution(* com.kavka.apiservices.controller.*.getAllByUser(..))")
    private void controllerGetAllByUser() {}

    @Pointcut("execution(* com.kavka.apiservices.controller.*.getByUser(..))")
    private void controllerGetByUser() {}

    @Pointcut("execution(* com.kavka.apiservices.controller.*.deleteByUser(..))")
    private void controllerDeleteByUser() {}

    private void throwIfIllegalUser(Integer userId, Authentication authentication) {
        User user = userService.getById(userId);
        if(!authentication.getName().equals(adminEmail) && !user.getEmail().equalsIgnoreCase(authentication.getName()))
            throw new InvalidOperationException(illegalSave);
    }

    @Before("controllerGetAllByUser() || controllerGetByUser() || controllerDeleteByUser()")
    public void ifResourceForUserInController(JoinPoint joinpoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object[] args = joinpoint.getArgs();
        if (args.length > 0) {
            Integer userId = (Integer) args[0];

            if (!this.userService.verifyIfCanAccessResource(userId, authentication))
                throw new InvalidOperationException(illegalResourceMessage);

        }
    }

    @Before("execution(* com.kavka.apiservices.controller.*.save(..))")
    public void ifSaveForIsUserInController(JoinPoint joinpoint) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object[] args = joinpoint.getArgs();
        String declaringTypeName =
                joinpoint.getSignature().getDeclaringTypeName()
                        .substring(joinpoint.getSignature().getDeclaringTypeName().lastIndexOf(".") + 1);
        if (args.length > 0) {
            switch (declaringTypeName) {
                case "AddressController":
                    Address address = (Address) args[0];
                    throwIfIllegalUser(address.getUser().getId(), authentication);
                    break;
                case "CreditApplicationDetailController":
                    CreditApplicationDetail creditApplicationDetail = (CreditApplicationDetail) args[0];
                    throwIfIllegalUser(creditApplicationDetail.getUser().getId(), authentication);
                    break;
            }
        }
    }
}
