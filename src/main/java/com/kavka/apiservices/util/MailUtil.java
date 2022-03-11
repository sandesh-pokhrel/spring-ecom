package com.kavka.apiservices.util;

import com.kavka.apiservices.common.MailProperties;
import com.kavka.apiservices.common.MailType;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

@Component
@AllArgsConstructor
public class MailUtil {

    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    private MimeMessage getMessageFormat(String toEmail, MailType mailType, String serial,
                                         byte[] attachmentBytes) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
//        helper.setFrom(mailProperties.getUsername());
        helper.setFrom("no-reply@kavka.com");
        helper.setTo(toEmail);

        if (mailType == MailType.USER_CREATION) {
            helper.setSubject("Registration Successful");
            String bodyText = "<h2>Congratulations you are successfully registered!</h2>";
            bodyText += "<h3>You can order your desired items now. Your order will be delivered in time.</h3>";
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
