package com.example.demo.configuration;

import com.example.demo.dto.request.authenticationRequest.IntrospectRequest;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Slf4j
@Component
public class CustomJwtDecoder implements JwtDecoder {
    private static final String ALGORITHM = "HS512";

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Autowired
    private AuthenticationService authenticationService;

    private NimbusJwtDecoder jwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            if (!authenticationService.introspect(IntrospectRequest.builder()
                    .token(token)
                    .build()).isValid()) {
                throw new AppException(ErrorCode.TOKEN_INVALID);
            }
        } catch (JOSEException | ParseException e) {
            log.error(e.getMessage());
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }

        try {
            // Khởi tạo NimbusJwtDecoder nếu chưa có
            if (Objects.isNull(jwtDecoder)) {
                jwtDecoder = initializeNimbusJwtDecoder();
            }

            // Decode JWT
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            log.error("Lỗi khi giải mã token: {}", e.getMessage());
            throw new AppException(ErrorCode.TOKEN_INVALID);
        }
    }

    private NimbusJwtDecoder initializeNimbusJwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), ALGORITHM);
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}
