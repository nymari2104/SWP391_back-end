package com.example.demo.configuration;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderDetail;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.VerificationTokenRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Random;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailSender {
    JavaMailSender javaMailSender;
    VerificationTokenRepository verificationTokenRepository;

    @NonFinal
    @Value("${spring.mail.username}")
    protected String SENDER_EMAIL;


    public int sendSixDigitOtp(String to, String subject, String body){
        Random random = new Random();
        int otp;

        do {
            //random otp 6 digit number
            otp = random.nextInt(100000, 999999);
        }while (verificationTokenRepository.existsById(otp));//Generate new if this otp has been created

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body + otp + "\n\nThis code is valid for the next 10 minutes. If you did not request this code, please disregard this email.\n\n" +
                "Best regards,\n" +
                "The Izumiya Team");
        simpleMailMessage.setFrom(SENDER_EMAIL);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new AppException(ErrorCode.SENDER_EMAIL_IS_NOT_AUTHORIZED);
        }
        return otp;
    }

    @Async
    public void sendOrderEmail(Order order) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(order.getEmail());
        helper.setSubject("Purchase Invoice");
        helper.setText(generateOrderHtml(order), true);
        helper.setFrom(SENDER_EMAIL);

        javaMailSender.send(message);
    }

    private String generateOrderHtml(Order order) {
        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<html><head><style>");
        // Common CSS
        htmlContent.append("table {font-family: Arial, sans-serif; border-collapse: collapse; width: 100%;}");
        htmlContent.append("td, th {border: 1px solid #ddd; padding: 8px;}");
        htmlContent.append("th {background-color: #f2f2f2; text-align: left;}");
        htmlContent.append(".title {color: #333; font-size: 24px;}");
        htmlContent.append(".summary {font-weight: bold;}");
        htmlContent.append("</style></head><body>");

        // Email content
        htmlContent.append("<h2 class='title'>Order Details</h2>");
        htmlContent.append("<table>");
        htmlContent.append("<tr><th>Order ID:</th><td>").append(order.getOrderId()).append("</td></tr>");
        htmlContent.append("<tr><th>Payment ID:</th><td>").append(order.getPaymentId()).append("</td></tr>");
        htmlContent.append("<tr><th>Customer Name:</th><td>").append(order.getFullname()).append("</td></tr>");
        htmlContent.append("<tr><th>Phone Number:</th><td>").append(order.getPhone()).append("</td></tr>");
        htmlContent.append("<tr><th>Email:</th><td>").append(order.getEmail()).append("</td></tr>");
        htmlContent.append("<tr><th>Address:</th><td>").append(order.getAddress()).append("</td></tr>");
        htmlContent.append("<tr><th>Status:</th><td>").append(order.getStatus()).append("</td></tr>");
        htmlContent.append("<tr><th>Creation Date:</th><td>").append(order.getCreateDate()).append("</td></tr>");
        htmlContent.append("</table>");

        // Order details
        htmlContent.append("<h3 class='title'>Product Details</h3>");
        htmlContent.append("<table>");
        htmlContent.append("<tr><th>Product</th><th>Quantity</th><th>Price</th></tr>");
        for (OrderDetail detail : order.getOrderDetails()) {
            htmlContent.append("<tr>");
            htmlContent.append("<td>").append(detail.getProductName()).append("</td>");
            htmlContent.append("<td>").append(detail.getQuantity()).append("</td>");
            htmlContent.append("<td>").append(detail.getTotal()).append("</td>");
            htmlContent.append("</tr>");
        }
        htmlContent.append("</table>");

        htmlContent.append("</body></html>");

        return htmlContent.toString();
    }
}
