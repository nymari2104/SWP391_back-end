package com.example.demo.service;

import com.example.demo.dto.request.SignInRequest;
import com.example.demo.dto.request.IntrospectRequest;
import com.example.demo.dto.request.LogoutRequest;
import com.example.demo.dto.response.SignInResponse;
import com.example.demo.dto.response.IntrospectResponse;
import com.example.demo.entity.InvalidatedToken;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.InvalidatedTokenRepository;
import com.example.demo.repository.UserRepository;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    UserMapper userMapper;


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
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authenticated)
            throw new AppException(ErrorCode.LOGIN_FAIL);//no match

        //create token for this login time
        var token = generateToken(user);

        return SignInResponse.builder()
                .token(token)
                .user(userMapper.toUserResponse(user))
                .authenticated(true)
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
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
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
