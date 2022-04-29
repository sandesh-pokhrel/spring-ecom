package com.kavka.apiservices.controller;

import com.kavka.apiservices.model.CreditApplicationDetail;
import com.kavka.apiservices.model.User;
import com.kavka.apiservices.request.CreditApplicationVerificationRequest;
import com.kavka.apiservices.service.CreditApplicationDetailService;
import com.kavka.apiservices.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/credit-applications")
@RequiredArgsConstructor
public class CreditApplicationDetailController {

    private final CreditApplicationDetailService creditApplicationDetailService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreditApplicationDetail save(@Valid @RequestBody CreditApplicationDetail creditApplicationDetail) {
        return this.creditApplicationDetailService.save(creditApplicationDetail);
    }

    @PostMapping("/verify/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void verifyApplication(@PathVariable Integer id,
                                  @Valid @RequestBody CreditApplicationVerificationRequest verificationRequest,
                                  Authentication authentication) {
        User verifiedBy = this.userService.getByEmail(authentication.getName());
        this.creditApplicationDetailService.verify(id, verificationRequest, verifiedBy);
    }
}
