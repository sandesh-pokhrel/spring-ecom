package com.kavka.apiservices.util;

import com.kavka.apiservices.common.MailProperties;
import com.kavka.apiservices.common.MailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailUtil {

    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    @Autowired
    public MailUtil(JavaMailSender javaMailSender, MailProperties mailProperties) {
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
    }

    @Value("${mail.message.registration}")
    private String registrationBody;

    private MimeMessage getMessageFormat(String toEmail, MailType mailType, String serial,
                                         byte[] attachmentBytes) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
//        helper.setFrom(mailProperties.getUsername());
        helper.setFrom("no-reply@kavka.com");
        helper.setTo(toEmail);

        if (mailType == MailType.USER_CREATION) {
            helper.setSubject("Registration Successful");
            String bodyText = registrationBody;
            helper.setText(bodyText, true);

        } else if (mailType == MailType.RESET_PASSWORD) {
            System.out.println("Reset password request");
        }
        return message;
    }

    @Async("threadPoolTaskExecutor")
    public void sendMail(String toEmail, MailType mailType, String serialNumber, byte[] attachmentBytes)
            throws MessagingException {
        MimeMessage message = getMessageFormat(toEmail, mailType, serialNumber, attachmentBytes);
        javaMailSender.send(message);
    }
}
