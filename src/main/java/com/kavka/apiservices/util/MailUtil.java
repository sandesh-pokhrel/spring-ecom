package com.kavka.apiservices.util;

import com.kavka.apiservices.common.MailProperties;
import com.kavka.apiservices.common.MailType;
import com.kavka.apiservices.model.Order;
import com.lowagie.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
@RequiredArgsConstructor
public class MailUtil {

    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    @Value("${mail.message.registration}")
    private String registrationBody;
    @Value("${mail.message.verification}")
    private String verificationBody;
    @Value("${mail.message.invoice}")
    private String invoiceBody;


    @SuppressWarnings({"rawtypes", "unchecked"})
    private void executeCallback(Map<String, Object> extras) {
        Object callback = extras.get("callback");
        if (callback instanceof Consumer) {
            ((Consumer) callback).accept(extras.get("data"));
        }
    }

    private byte[] generatePdfFromHtml(String html) throws DocumentException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(byteArrayOutputStream, false);
        renderer.finishPDF();
        return byteArrayOutputStream.toByteArray();
    }

    private String invoiceBodyFromTemplate(String template, Order order) {
        AtomicReference<Double> atomicReference = new AtomicReference<>(0D);
        String items = order.getOrderItems().stream().map(orderItem -> {
            Double totalPrice = orderItem.getQuantity() * orderItem.getProductDetail().getPrice();
            atomicReference.set(atomicReference.get() + totalPrice);
            String i = "<tr>";
            i += "<td>" + orderItem.getProductDetail().getName() + "</td>";
            i += "<td>" + orderItem.getQuantity() + "</td>";
            i += "<td>" + orderItem.getProductDetail().getPrice() + "</td>";
            i += "<td>" + totalPrice + "</td>";
            i += "</tr>";
            return i;
        }).collect(Collectors.joining(""));
        return template
                .replace("{##tableContents##}", items)
                .replace("{##orderId##}", String.valueOf(order.getId()))
                .replace("{##orderDate##}", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))
                .replace("{##customerId##}", order.getUser().getEmail())
                .replace("{##tableTotal##}", String.valueOf(atomicReference.get()));
    }


    private MimeMessage getMessageFormat(String toEmail, MailType mailType, Map<String, Object> extras)
            throws MessagingException, DocumentException {
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
        } else if (mailType == MailType.USER_VERIFICATION) {
            helper.setSubject("Verification Successful");
            String bodyText = verificationBody;
            helper.setText(bodyText, true);
        } else if (mailType == MailType.INVOICE_MAIL) {
            helper.setSubject("Invoice");
            String bodyText = invoiceBodyFromTemplate(invoiceBody, (Order) extras.get("data"));
            helper.setText(bodyText, true);
//            attachmentBytes = generatePdfFromHtml(bodyText);
//            helper.addAttachment("Invoice.pdf",
//                    new ByteArrayDataSource(attachmentBytes, "application/octet-stream"));
        }
        return message;
    }

    @Async("threadPoolTaskExecutor")
    public void sendMail(String toEmail, MailType mailType, Map<String, Object> extras)
            throws MessagingException, DocumentException {
        MimeMessage message = getMessageFormat(toEmail, mailType, extras);
        javaMailSender.send(message);
        if (Objects.nonNull(extras) && extras.containsKey("callback")) {
            executeCallback(extras);
        }
    }
}
