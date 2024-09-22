package com.example.demo.service;

import com.example.demo.dto.request.SignUpRequest;
import com.example.demo.dto.request.UserUpdateRequest;
import com.example.demo.dto.response.SignUpResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.VerificationMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    VerificationTokenRepository verificationTokenRepository;
    VerificationMapper verificationMapper;
    JavaMailSender javaMailSender;

    @NonFinal
    @Value("${spring.mail.username}")
    protected String SENDER_EMAIL;

    public SignUpResponse createUser(SignUpRequest request){
        // Check username
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        //Map data from SignUpRequest to VerificationToken
        VerificationToken verificationToken = verificationMapper.toVerificationToken(request);
        //Encoder password
        verificationToken.setPassword(passwordEncoder.encode(request.getPassword()));
        Random random = new Random();
        int otp;
        do {
            otp = random.nextInt(100000, 999999);
        }while (verificationTokenRepository.existsById(otp));
        verificationToken.setOtp(otp);
        verificationToken.setExpiryTime(new Date(
                Instant.now().plus(1, ChronoUnit.MINUTES).toEpochMilli()
        ));

        verificationTokenRepository.save(verificationToken);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        String to = "manhcat24@gmail.com";
        String subject = "Test verify email";
        String body = String.valueOf(otp);

        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(body);
        simpleMailMessage.setFrom(SENDER_EMAIL);

        javaMailSender.send(simpleMailMessage);


//        try {
//            user = userRepository.save(user);
//        } catch (DataIntegrityViolationException e) {
//            throw new AppException(ErrorCode.EMAIL_EXISTED);
//        }

        return verificationMapper.toSignUpResponse(verificationToken);
    }


    public UserResponse getMyInfo(){
        var context = SecurityContextHolder.getContext();//when user login success, user info will be store in SecurityContextHolder
        String email = context.getAuthentication().getName();//get email of user

        User user = userRepository.findByEmail(email)//get User object by email
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));//user not existed

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateMyInfo(UserUpdateRequest request){
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();
        log.info(email);
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));
        userMapper.updateUser(user, request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));
        userMapper.updateUser(user, request);
        //set password after encode
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String userId){
        userRepository.deleteById(userId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers(){
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse).toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @PostAuthorize("returnObject.username == authentication.name")//check if username is match with who is login
    public UserResponse getUser(String Id){
        return userMapper.toUserResponse(userRepository.findById(Id).orElseThrow(() -> new AppException(ErrorCode.USER_ID_NOT_EXISTED)));
    }

}
