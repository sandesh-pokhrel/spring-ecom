package com.kavka.apiservices.service;

import com.kavka.apiservices.dto.mapper.OrderToDtoMapper;
import com.kavka.apiservices.model.Address;
import com.kavka.apiservices.model.OrderRequestMode;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.model.mapper.OrderRequestItemToModelMapper;
import com.kavka.apiservices.model.mapper.OrderRequestToModelMapper;
import com.kavka.apiservices.model.mapper.PaymentRequestToModelMapper;
import com.kavka.apiservices.repository.OrderRepository;
import com.kavka.apiservices.request.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private AddressService addressService;
    @Mock
    private UserService userService;
    @Mock
    private ProductDetailService productDetailService;
    @Mock
    private UserStoreCreditService userStoreCreditService;
    @Mock
    private Validator validator;
    @Mock
    private OrderToDtoMapper orderToDtoMapper;
    @Mock
    private OrderRequestItemToModelMapper orderRequestItemToModelMapper;

    @Mock
    PaymentRequestToModelMapper paymentRequestToModelMapper;

    @Mock
    OrderRequestToModelMapper orderRequestToModelMapper;

    @Mock
    RestTemplate restTemplate;

    private ConstraintViolation<Address> setupConstraintViolation() {
        return new ConstraintViolation<Address>() {
            @Override
            public String getMessage() {
                return "violation happened";
            }

            @Override
            public String getMessageTemplate() {
                return null;
            }

            @Override
            public Address getRootBean() {
                return null;
            }

            @Override
            public Class<Address> getRootBeanClass() {
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
        this.orderService = new OrderService(orderRepository, addressService, userService,
                productDetailService, userStoreCreditService, validator, orderToDtoMapper,
                orderRequestItemToModelMapper, paymentRequestToModelMapper,
                orderRequestToModelMapper, restTemplate);

        this.orderRequest = OrderRequest.builder()
                .orderRequestMode(OrderRequestMode.BASIC)
                .billingId(4)
                .build();
    }

    @Test
    void validateCustomer_fail() {
        Address billing = Address.builder()
                .id(1001)
                .firstName(null)
                .lastName("some_last_name")
                .company("some_company")
                .build();
        Set<ConstraintViolation<Address>> violations = new HashSet<>();
        violations.add(setupConstraintViolation());
        when(validator.validate(any(Address.class))).thenReturn(violations);
        assertThrows(ConstraintViolationException.class, () -> this.orderService.validateCustomer(billing));
    }

    @Test
    void validateCustomer_pass() {
        Address billing = Address.builder()
                .id(1001)
                .firstName("some_first_name")
                .lastName("some_last_name")
                .company("some_company")
                .build();
        Set<ConstraintViolation<Address>> violations = new HashSet<>();
        when(validator.validate(any(Address.class))).thenReturn(violations);
        assertDoesNotThrow(() -> this.orderService.validateCustomer(billing));
    }

    //@Test
    void getBilling_default_mode() {
        Address billing = Address.builder().id(1005).build();
        when(this.addressService.getByEmailAndIsDefault(anyString(), anyBoolean())).thenReturn(billing);
        //Address billing1 = this.orderService.getAdd(orderRequest, "some_email@email.com");
//        assertNotNull(billing1, "Billing cannot be null");
//        assertEquals(1005, billing1.getId());

    }

    //@Test
    void saveOrder() {
        Address billing = Address.builder().id(1005).build();
        User user = User.builder().id(1005).build();
        when(this.addressService.getByEmailAndIsDefault(anyString(), anyBoolean())).thenReturn(billing);
        //Address billing1 = this.orderService.getBilling(orderRequest, "some_email@email.com");

    }
}
