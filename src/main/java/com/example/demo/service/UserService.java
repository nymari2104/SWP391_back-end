package com.example.demo.service;

import com.example.demo.configuration.EmailSender;
import com.example.demo.dto.request.authenticationRequest.SignUpRequest;
import com.example.demo.dto.request.userRequest.ForgotPasswordRequest;
import com.example.demo.dto.request.userRequest.ResetPasswordRequest;
import com.example.demo.dto.request.userRequest.UpdatePasswordRequest;
import com.example.demo.dto.request.userRequest.UserUpdateRequest;
import com.example.demo.dto.response.authenticationResponse.SignUpResponse;
import com.example.demo.dto.response.userResponse.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.enums.Role;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.VerificationMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
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
    EmailSender emailSender;

    public SignUpResponse createUser(SignUpRequest request){
        // Check username
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        //Map data from SignUpRequest to VerificationToken
        VerificationToken verificationToken = verificationMapper.toVerificationToken(request);
        //Encoder password
        verificationToken.setPassword(passwordEncoder.encode(request.getPassword()));

        String to = request.getEmail();
        String subject = "Verify Your Email!";
        String body = ("Dear " + request.getEmail() + ",\n\n" +
                "Thank you for signing up! To complete your registration, please verify your email address by using the following OTP (One-Time Password):\n\n" +
                "\"Verification Code: ");

        int otp = emailSender.sendSixDigitOtp(to, subject, body);

        //Set otp
        verificationToken.setOtp(otp);
        //Set expiry time after 10 minute
        verificationToken.setExpiryTime(new Date(
                Instant.now().plus(10, ChronoUnit.MINUTES).toEpochMilli()));

        verificationTokenRepository.save(verificationToken);

        return verificationMapper.toSignUpResponse(verificationToken);
    }

    public UserResponse getMyInfo(){
        return userMapper.toUserResponse(getCurrentUser());
    }

    public UserResponse updateMyInfo(UserUpdateRequest request){
        User user = getCurrentUser();

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void updateMyPassword(UpdatePasswordRequest request){
        User user = getCurrentUser();
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
            throw new AppException(ErrorCode.WRONG_PASSWORD);
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword()))
            throw new AppException(ErrorCode.MATCH_OLD_PASSWORD);
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
    }

    public void resetPassword(ResetPasswordRequest request){
        //Get user who has been verified forgot password
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));
        //Check if reset password matches the old
        if (passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new AppException(ErrorCode.MATCH_OLD_PASSWORD);
        //encode password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse createAdminAccount(SignUpRequest request){
        //Map request to User
        User user = userMapper.toUser(request);
        //Encode password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        //Set role Admin for account
        user.setRole(Role.ADMIN.name());
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));
        userMapper.updateUser(user, request);
        //set password after encode
        if (request.getPassword() != null)
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
    public UserResponse getUser(String Id){
        return userMapper.toUserResponse(userRepository.findById(Id).orElseThrow(() -> new AppException(ErrorCode.USER_ID_NOT_EXISTED)));
    }

    public String forgotPassword(ForgotPasswordRequest request){
        //Check username
        String email = request.getEmail();
        if (!userRepository.existsByEmail(email)) {
            throw new AppException(ErrorCode.EMAIL_NOT_EXISTED);
        }

        String subject = "Forgot password";
        String body = ("Hello " + email + ",\n\n" +
                "We have received a request to reset the password for your account. " +
                "Please use the following 6-digit OTP to verify your identity:\n\n");

        int otp = emailSender.sendSixDigitOtp(email, subject, body);

        verificationTokenRepository.save(VerificationToken.builder()
                .email(email)
                .otp(otp)
                .expiryTime(new Date(Instant.now()
                        .plus(5, ChronoUnit.MINUTES).toEpochMilli()))
                .build());

        return email;
    }

    public User getCurrentUser(){
        var context = SecurityContextHolder.getContext();//when user login success, user info will be store in SecurityContextHolder
        String email = context.getAuthentication().getName();//get email of user

        return userRepository.findByEmail(email)//get User object by email
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));//user not existed
    }
}
