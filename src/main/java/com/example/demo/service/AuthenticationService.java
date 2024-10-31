package com.example.demo.service;

import com.example.demo.dto.request.authenticationRequest.*;
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
import com.example.demo.mapper.VerificationTokenMapper;
import com.example.demo.repository.InvalidatedTokenRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.VerificationTokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
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
    VerificationTokenMapper verificationTokenMapper;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SECRET_KEY;


    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        //get token
        var token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token);
        } catch (Exception e) {
            log.error(e.getMessage());
            isValid = false;
        }
        return IntrospectResponse.builder()
                .valid(isValid).build();
    }

    public SignInResponse authenticate(SignInRequest request) {
        //check username
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.LOGIN_FAIL));
        if (!user.isStatus())
            throw new AppException(ErrorCode.USER_INACTIVE);
        //check match password
        if (!checkMatchPassword(request.getPassword(), user.getPassword()))
            throw new AppException(ErrorCode.LOGIN_FAIL);//no match

        //createOrderDetail token for this login time
        var token = generateToken(user);

        return SignInResponse.builder()
                .token(token)
                .user(userMapper.toUserResponse(user))
                .build();
    }

    public SignInResponse authenticate(String accessToken) throws JsonProcessingException {
        //Get user info from access token
        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(userInfoEndpoint, String.class);
        //Check if access token is valid
        if (response.getStatusCode() != HttpStatus.OK)
            throw new AppException(ErrorCode.TOKEN_INVALID);
        //Convert response to JsonNode to get field of info
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userInfoJson = objectMapper.readTree(response.getBody());
        // Lấy các trường cụ thể từ JsonNode
        String email = userInfoJson.get("email").asText();
        String name = userInfoJson.get("name").asText();
        //Check if user exist
        User checkUser = userRepository.findByEmail(email)
                .orElseGet(() -> User.builder()
                        .fullname(name)
                        .email(email)
                        .role(Role.USER.name())
                        .googleAccount(true)
                        .status(true)
                        .build());
        //check if user is not google account
        if (!checkUser.isGoogleAccount()) {
            throw new AppException(ErrorCode.EMAIL_EXISTED);
        }else if(!checkUser.isStatus()){
            throw new AppException(ErrorCode.USER_INACTIVE);
        }
        else if(checkUser.getUserId() == null){
            checkUser = userRepository.save(checkUser);
        }

        var token = generateToken(checkUser);

        return SignInResponse.builder()
                .user(userMapper.toUserResponse(checkUser))
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

    public UserResponse verifySignUp(VerifyOtpRequest request) {
        //Verify otp
        VerificationToken verificationToken = verifyOtp(request);

        User user = verificationTokenMapper.toUser(verificationToken);
        user.setRole(Role.USER.toString());
        user.setGoogleAccount(false);

        user = userRepository.save(user);

        return userMapper.toUserResponse(user);
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

        if (!(verified && expiryTime.after(new Date())))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        //5. Check if token has been logout
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.TOKEN_INVALID);

        return signedJWT;
    }

    public boolean checkMatchPassword(String requestPassword, String userPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        return passwordEncoder.matches(requestPassword, userPassword);
    }

    public VerificationToken verifyOtp(VerifyOtpRequest request) {
        //get verificationToken
        VerificationToken verificationToken = verificationTokenRepository.findByOtp(request.getOtp())
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_OTP_INVALID));
        //check if this otp is generated for sign-up email
        if (!verificationToken.getEmail().equals(request.getEmail()))
            throw new AppException(ErrorCode.EMAIL_OTP_INVALID);
        //check if otp is expired
        if (!verificationToken.getExpiryTime().after(new Date()))
            throw new AppException(ErrorCode.EMAIL_OTP_EXPIRED);
        return verificationToken;
    }

    private String generateToken(User user) {
        //1. Create jwt header
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
        //2. Create jwt payload
        //2.1 Set jwt claims
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer("izumiya.com")
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
            return jwsObject.serialize();//serialize jws to jwt
        } catch (JOSEException e) {
            log.error("Cannot createOrderDetail token", e);
            throw new RuntimeException(e);
        }
    }

}
