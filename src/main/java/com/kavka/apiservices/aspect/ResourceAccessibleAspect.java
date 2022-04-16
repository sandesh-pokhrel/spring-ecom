package com.kavka.apiservices.aspect;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    @Before("execution(* com.kavka.apiservices.controller.*.getAllByUser(..))")
    public void ifResourceForUserInController(JoinPoint joinpoint) {
        log.warn("Before aspect to check if resource is for given user...");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object[] args = joinpoint.getArgs();
        if (args.length > 0) {
            if (joinpoint.getSignature().getDeclaringTypeName().contains("OrderController")) {
                Integer userId = (Integer) args[0];

                if (!this.userService.verifyIfCanAccessResource(userId, authentication))
                    throw new InvalidOperationException(illegalResourceMessage);
            }
        }
    }
}
