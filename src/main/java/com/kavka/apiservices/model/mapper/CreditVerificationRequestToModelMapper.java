package com.kavka.apiservices.model.mapper;

import com.kavka.apiservices.model.CreditApplicationDetail;
import com.kavka.apiservices.model.UserStoreCredit;
import com.kavka.apiservices.request.CreditApplicationVerificationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface CreditVerificationRequestToModelMapper {

    @Mapping(target = "creditApplicationDetail", source = "creditApplicationDetail")
    @Mapping(target = "currentBalance", constant = "0D")
    @Mapping(target = "availableBalance", source = "verificationRequest.creditLine")
    UserStoreCredit from(CreditApplicationVerificationRequest verificationRequest,
                         CreditApplicationDetail creditApplicationDetail);
}
