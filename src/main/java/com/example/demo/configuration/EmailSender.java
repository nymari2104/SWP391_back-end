package com.example.demo.configuration;

import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.VerificationTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
            throw new AppException(ErrorCode.EMAIL_NOT_EXISTED);
        }
        return otp;
    }
}
