package com.kavka.apiservices.service;

import com.kavka.apiservices.dto.mapper.OrderToDtoMapper;
import com.kavka.apiservices.model.Billing;
import com.kavka.apiservices.model.OrderRequestMode;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.model.mapper.OrderRequestItemToModelMapper;
import com.kavka.apiservices.model.mapper.PaymentRequestToModelMapper;
import com.kavka.apiservices.repository.OrderRepository;
import com.kavka.apiservices.request.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    private OrderService orderService;
    private OrderRequest orderRequest;

    @Mock
    private Authentication authentication;
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

    @Mock
    PaymentRequestToModelMapper paymentRequestToModelMapper;

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
                orderToDtoMapper, orderRequestItemToModelMapper, paymentRequestToModelMapper);

        this.orderRequest = OrderRequest.builder()
                .orderRequestMode(OrderRequestMode.DEFAULT)
                .billingId(4)
                .build();
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
    void getBilling_default_mode() {
        Billing billing = Billing.builder().id(1005).build();
        when(this.billingService.getByEmailAndIsDefault(anyString(), anyBoolean())).thenReturn(billing);
        Billing billing1 = this.orderService.getBilling(orderRequest, "some_email@email.com");
        assertNotNull(billing1, "Billing cannot be null");
        assertEquals(1005, billing1.getId());

    }

    @Test
    void saveOrder() {
        Billing billing = Billing.builder().id(1005).build();
        User user = User.builder().id(1005).build();
        when(this.billingService.getByEmailAndIsDefault(anyString(), anyBoolean())).thenReturn(billing);
        Billing billing1 = this.orderService.getBilling(orderRequest, "some_email@email.com");

    }
}
