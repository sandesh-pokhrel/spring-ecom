package com.kavka.apiservices.service;

import com.kavka.apiservices.dto.mapper.OrderToDtoMapper;
import com.kavka.apiservices.model.Billing;
import com.kavka.apiservices.model.mapper.OrderRequestItemToModelMapper;
import com.kavka.apiservices.repository.OrderRepository;
import com.kavka.apiservices.request.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContext;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;

    @Mock
    private SecurityContext securityContext;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private BillingService billingService;
    @Mock
    private UserService userService;
    @Mock
    private ProductDetailService productDetailService;
    @Mock
    private Validator validator;
    @Mock
    private OrderToDtoMapper orderToDtoMapper;
    @Mock
    private OrderRequestItemToModelMapper orderRequestItemToModelMapper;

    private ConstraintViolation<Billing> setupConstraintViolation() {
        return new ConstraintViolation<Billing>() {
            @Override
            public String getMessage() {
                return "violation happened";
            }

            @Override
            public String getMessageTemplate() {
                return null;
            }

            @Override
            public Billing getRootBean() {
                return null;
            }

            @Override
            public Class<Billing> getRootBeanClass() {
                return null;
            }

            @Override
            public Object getLeafBean() {
                return null;
            }

            @Override
            public Object[] getExecutableParameters() {
                return new Object[0];
            }

            @Override
            public Object getExecutableReturnValue() {
                return null;
            }

            @Override
            public Path getPropertyPath() {
                return null;
            }

            @Override
            public Object getInvalidValue() {
                return null;
            }

            @Override
            public ConstraintDescriptor<?> getConstraintDescriptor() {
                return null;
            }

            @Override
            public <U> U unwrap(Class<U> aClass) {
                return null;
            }
        };
    }

    @BeforeEach
    void setup() {
        this.orderService = new OrderService(orderRepository, billingService, userService,
                productDetailService, validator,
                orderToDtoMapper, orderRequestItemToModelMapper);
    }

    @Test
    void validateCustomer_fail() {
        Billing billing = Billing.builder()
                .id(1001)
                .firstName(null)
                .lastName("some_last_name")
                .company("some_company")
                .build();
        Set<ConstraintViolation<Billing>> violations = new HashSet<>();

        violations.add(setupConstraintViolation());

        when(validator.validate(any(Billing.class))).thenReturn(violations);

        assertThrows(ConstraintViolationException.class, () -> this.orderService.validateCustomer(billing));
    }

    @Test
    void validateCustomer_pass() {
        Billing billing = Billing.builder()
                .id(1001)
                .firstName("some_first_name")
                .lastName("some_last_name")
                .company("some_company")
                .build();
        Set<ConstraintViolation<Billing>> violations = new HashSet<>();

        when(validator.validate(any(Billing.class))).thenReturn(violations);

        assertDoesNotThrow(() -> this.orderService.validateCustomer(billing));
    }


    @Test
    void saveOrder() {

        when(securityContext.getAuthentication()).thenReturn(null);

    }
}
