package com.kavka.apiservices.service;

import com.kavka.apiservices.exception.InvalidOperationException;
import com.kavka.apiservices.exception.NotFoundException;
import com.kavka.apiservices.model.CreditApplicationDetail;
import com.kavka.apiservices.model.CreditApplicationStatus;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.model.UserStoreCredit;
import com.kavka.apiservices.repository.CreditApplicationDetailRepository;
import com.kavka.apiservices.request.CreditApplicationVerificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CreditApplicationDetailService {

    private final CreditApplicationDetailRepository creditApplicationDetailRepository;
    private final UserService userService;
    private final UserStoreCreditService userStoreCreditService;

    public CreditApplicationDetail getById(Integer id) {
        return this.creditApplicationDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Credit application not found!"));
    }

    public CreditApplicationDetail getByUser(User user) {
        return this.creditApplicationDetailRepository.findByUser(user).orElse(null);
    }

    public CreditApplicationDetail getByEIN(String EIN) {
        return this.creditApplicationDetailRepository.findByEIN(EIN).orElse(null);
    }

    public CreditApplicationDetail save(CreditApplicationDetail creditApplicationDetail) {
        User user = creditApplicationDetail.getUser();
        if (Objects.nonNull(getByUser(user)) || Objects.nonNull(getByEIN(creditApplicationDetail.getEIN())))
            throw new InvalidOperationException("Application already exists for this user or EIN!");
        creditApplicationDetail.setCreditApplicationStatus(CreditApplicationStatus.PENDING);
        return this.creditApplicationDetailRepository.save(creditApplicationDetail);
    }

    public String verify(Integer id,
                       CreditApplicationVerificationRequest verificationRequest,
                       User verifiedBy) {
        CreditApplicationDetail creditApplicationDetail = this.getById(id);
        if (creditApplicationDetail.getCreditApplicationStatus() != CreditApplicationStatus.PENDING)
            throw new InvalidOperationException("Application is already verified!");
        if (verificationRequest.getIsApproved() && Objects.isNull(verificationRequest.getCreditLine()))
            throw new InvalidOperationException("Credit line cannot be null for approved application!");

        // set credit application detail fields
        creditApplicationDetail.setCreditApplicationStatus((verificationRequest.getIsApproved()
                ? CreditApplicationStatus.APPROVED : CreditApplicationStatus.DECLINED));
        creditApplicationDetail.setVerifiedOn(new Date());
        creditApplicationDetail.setVerifiedBy(verifiedBy);
        creditApplicationDetail.setVerificationNote(verificationRequest.getVerificationNote());

        // set up store credit
        if (!verificationRequest.getIsApproved()) {
            this.creditApplicationDetailRepository.save(creditApplicationDetail);
        } else {
            UserStoreCredit userStoreCredit = UserStoreCredit.builder()
                    .creditApplicationDetail(creditApplicationDetail)
                    .creditLine(verificationRequest.getCreditLine())
                    .availableBalance(verificationRequest.getCreditLine())
                    .currentBalance(0D)
                    .user(creditApplicationDetail.getUser())
                    .build();
            this.userStoreCreditService.save(userStoreCredit);
        }
        return creditApplicationDetail.getUser().getEmail();
    }
}
