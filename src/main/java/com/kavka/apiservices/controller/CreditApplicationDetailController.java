package com.kavka.apiservices.controller;

import com.kavka.apiservices.common.MailType;
import com.kavka.apiservices.model.CreditApplicationDetail;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.request.CreditApplicationVerificationRequest;
import com.kavka.apiservices.service.CreditApplicationDetailService;
import com.kavka.apiservices.service.UserService;
import com.kavka.apiservices.util.MailUtil;
import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/credit-applications")
@RequiredArgsConstructor
public class CreditApplicationDetailController {

    private final CreditApplicationDetailService creditApplicationDetailService;
    private final UserService userService;
    private final MailUtil mailUtil;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreditApplicationDetail save(@Valid @RequestBody CreditApplicationDetail creditApplicationDetail) {
        return this.creditApplicationDetailService.save(creditApplicationDetail);
    }

    @PostMapping("/verify/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void verifyApplication(@PathVariable Integer id,
                                  @Valid @RequestBody CreditApplicationVerificationRequest verificationRequest,
                                  Authentication authentication) throws MessagingException, DocumentException {
        User verifiedBy = this.userService.getByEmail(authentication.getName());
        String userEmail = this.creditApplicationDetailService.verify(id, verificationRequest, verifiedBy);
        this.mailUtil.sendMail(userEmail, MailType.USER_CREDIT_VERIFICATION, new HashMap<String, Object>() {{
            put("data", verificationRequest.getIsApproved());
        }});
    }
}
