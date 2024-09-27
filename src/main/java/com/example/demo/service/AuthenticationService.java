package com.example.demo.service;

import com.example.demo.dto.request.authenticationRequest.*;
import com.example.demo.dto.request.userRequest.ResetPasswordRequest;
import com.example.demo.dto.request.userRequest.UserUpdateRequest;
import com.example.demo.dto.response.authenticationResponse.SignInResponse;
import com.example.demo.dto.response.authenticationResponse.IntrospectResponse;
import com.example.demo.dto.response.userResponse.UserResponse;
import com.example.demo.entity.InvalidatedToken;
import com.example.demo.entity.User;
import com.example.demo.entity.VerificationToken;
import com.example.demo.enums.Role;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.mapper.VerificationMapper;
import com.example.demo.repository.InvalidatedTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    UserMapper userMapper;
    VerificationTokenRepository verificationTokenRepository;
    VerificationMapper verificationMapper;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SECRET_KEY;


    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        //get token
        var token = request.getToken();
        //check token
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (ParseException e) {
            isValid = false;
        }

        return IntrospectResponse.builder()
                .valid(isValid).build();

    }

    public SignInResponse authenticate(SignInRequest request){
        //check username
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAIL));

        //check match password
        if (!checkMatchPassword(request.getPassword(), user.getPassword()))
            throw new AppException(ErrorCode.LOGIN_FAIL);//no match

        //create token for this login time
        var token = generateToken(user);

        return SignInResponse.builder()
                .token(token)
                .user(userMapper.toUserResponse(user))
                .build();
    }

    public SignInResponse authenticate(String email, String fullname){

        Optional<User> checkUser = userRepository.findByEmail(email);
        User user = User.builder()
                .fullname(fullname)
                .email(email)
                .role(Role.USER.name())
                .googleAccount(true)
                .build();
          if (checkUser.isPresent() && !checkUser.get().isGoogleAccount()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }else if (checkUser.isEmpty()){
            userRepository.save(user);
        }

        var token = generateToken(user);

        return SignInResponse.builder()
                .user(userMapper.toUserResponse(user))
                .token(token)
                .build();
    }

    public void Logout(LogoutRequest request) throws ParseException, JOSEException {
        //1. Read token
        //1.1 Verify token
        var token = verifyToken(request.getToken());
        //1.2 Get ID token
        String jit = token.getJWTClaimsSet().getJWTID();
        //1.3 Get expiry time
        Date expiryDate = token.getJWTClaimsSet().getExpirationTime();
        //2. store in DB
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .tokenId(jit)
                .expiryDate(expiryDate)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

    }

    public UserResponse verifySignUp(VerifyOtpRequest request){
        //Verify otp
        VerificationToken verificationToken = verifyOtp(request.getEmail(), request.getOtp());

        User user = verificationMapper.toUser(verificationToken);
        user.setRole(Role.USER.toString());
        user.setGoogleAccount(false);
        try {
            user = userRepository.save(user);
        } catch (Exception e) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }
         return userMapper.toUserResponse(user);
    }

    public void verifyResetPassword(ResetPasswordRequest request){
        VerifyOtpRequest verifyOtpRequest = request.getVerifyOtpRequest();
        UserUpdateRequest userUpdateRequest = request.getUserUpdateRequest();
        //verify otp

        verifyOtp(verifyOtpRequest.getEmail(), verifyOtpRequest.getOtp());


        User user = userRepository.findByEmail(verifyOtpRequest.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));
        //Check if reset password matches the old
        if (checkMatchPassword(userUpdateRequest.getPassword(), user.getPassword()))
            throw new AppException(ErrorCode.MATCH_OLD_PASSWORD);

        userMapper.updateUser(user, userUpdateRequest);
        //encode password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));

        userRepository.save(user);
    }

    public SignedJWT verifyToken(String token) throws ParseException, JOSEException {
        //1. Parse jwt token to SignedJWT to access jwt component
        SignedJWT signedJWT = SignedJWT.parse(token);

        //2. Get expiry time to check
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        //3. Create a verifier by using MACVerifier with SECRET_KEY
        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

        //4. Verify signature of signedJWT
        var verified = signedJWT.verify(verifier);

        if(!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        //5. Check if token has been logout
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.TOKEN_INVALID);
        return signedJWT;
    }

    private boolean checkMatchPassword(String requestPassword, String userPassword){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(requestPassword, userPassword);
    }

    private VerificationToken verifyOtp(String email, int otp){
        //get verificationToken
        VerificationToken verificationToken = verificationTokenRepository.findById(otp)
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_OTP_INVALID));
        //check if this otp is generated for sign-up email
        if (!verificationToken.getEmail().equals(email))
            throw new AppException(ErrorCode.EMAIL_OTP_INVALID);
        //check if otp is expired
        if (!verificationToken.getExpiryTime().after(new Date()))
            throw new AppException(ErrorCode.EMAIL_OTP_EXPIRED);
        return verificationToken;
    }

    private String generateToken(User user){
        //1. Create jwt header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        //2. Create jwt payload
        //2.1 Set jwt claims
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("demo.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(365, ChronoUnit.DAYS).toEpochMilli()
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", user.getRole())
                .build();
        //convert jwt claims to JSON to create a payload
        Payload payload = new Payload(claimsSet.toJSONObject());

        //3. Create jws(consist of jwt header and jwt payload)
        JWSObject jwsObject = new JWSObject(header, payload);
        try {
            //Sign this jws by using MACSigner with SECRET_KEY
            jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
            return jwsObject.serialize();//convert jws to jwt
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

}
