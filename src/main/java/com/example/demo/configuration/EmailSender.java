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
        simpleMailMessage.setText(body + otp + "\n\nThis code is valid for the next 3 minutes. If you did not request this code, please disregard this email.\n\n" +
                "Best regards,\n" +
                "The Izumiya Team");
        simpleMailMessage.setFrom(SENDER_EMAIL);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (MailException e) {
            throw new AppException(ErrorCode.SENDER_EMAIL_IS_NOT_PERMITTED);
        }
        return otp;
    }

    public void sendOrderEmail(Order order) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(order.getEmail());
        helper.setSubject("Hóa đơn mua hàng");
        helper.setText(generateOrderHtml(order), true);
        helper.setFrom(SENDER_EMAIL);

        javaMailSender.send(message);
    }

    private String generateOrderHtml(Order order) {
        StringBuilder htmlContent = new StringBuilder();

        htmlContent.append("<html><head><style>");
        // CSS chung
        htmlContent.append("table {font-family: Arial, sans-serif; border-collapse: collapse; width: 100%;}");
        htmlContent.append("td, th {border: 1px solid #ddd; padding: 8px;}");
        htmlContent.append("th {background-color: #f2f2f2; text-align: left;}");
        htmlContent.append(".title {color: #333; font-size: 24px;}");
        htmlContent.append(".summary {font-weight: bold;}");
        htmlContent.append("</style></head><body>");

        // Nội dung email
        htmlContent.append("<h2 class='title'>Chi tiết đơn hàng</h2>");
        htmlContent.append("<table>");
        htmlContent.append("<tr><th>Mã đơn hàng:</th><td>").append(order.getOrderId()).append("</td></tr>");
        htmlContent.append("<tr><th>Mã thanh toán:</th><td>").append(order.getPaymentId()).append("</td></tr>");
        htmlContent.append("<tr><th>Tên khách hàng:</th><td>").append(order.getFullname()).append("</td></tr>");
        htmlContent.append("<tr><th>Số điện thoại:</th><td>").append(order.getPhone()).append("</td></tr>");
        htmlContent.append("<tr><th>Email:</th><td>").append(order.getEmail()).append("</td></tr>");
        htmlContent.append("<tr><th>Địa chỉ:</th><td>").append(order.getAddress()).append("</td></tr>");
        htmlContent.append("<tr><th>Trạng thái:</th><td>").append(order.getStatus()).append("</td></tr>");
        htmlContent.append("<tr><th>Ngày tạo:</th><td>").append(order.getCreateDate()).append("</td></tr>");
        htmlContent.append("</table>");

        // Chi tiết đơn hàng
        htmlContent.append("<h3 class='title'>Chi tiết sản phẩm</h3>");
        htmlContent.append("<table>");
        htmlContent.append("<tr><th>Sản phẩm</th><th>Số lượng</th><th>Giá</th></tr>");
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
