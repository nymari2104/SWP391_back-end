package com.example.demo.controller;

import com.example.demo.dto.request.authenticationRequest.*;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.authenticationResponse.SignInResponse;
import com.example.demo.dto.response.authenticationResponse.IntrospectResponse;
import com.example.demo.dto.response.userResponse.UserResponse;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.service.AuthenticationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    ApiResponse<SignInResponse> signIn(@RequestBody SignInRequest request){
        var result = authenticationService.authenticate(request);
        return ApiResponse.<SignInResponse>builder()
                .message("Sign in successfully!")
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .message("Authenticate successfully!")
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.Logout(request);
        return ApiResponse.<Void>builder()
                .message("Logout")
                .build();
    }

    @PostMapping("/verify-sign-up")
    ApiResponse<UserResponse> verifySignUp(@RequestBody VerifyOtpRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("sign-up successfully!")
                .result(authenticationService.verifySignUp(request))
                .build();
    }

    @PostMapping("/verify-reset-password")
    ApiResponse<String> verifyResetPassword(@RequestBody VerifyOtpRequest request){
        return ApiResponse.<String>builder()
                .message("Verify successfully!")
                .result(authenticationService.verifyOtp(request).getEmail())
                .build();
    }

    @PostMapping("/sign-in-by-google")
    public ApiResponse<SignInResponse> loginSuccess(@RequestBody String request) throws IOException {
        String userInfoEndpoint = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + request;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(userInfoEndpoint, String.class);

        if (response.getStatusCode() != HttpStatus.OK)
            throw new AppException(ErrorCode.TOKEN_INVALID);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userInfoJson = objectMapper.readTree(response.getBody());

        // Lấy các trường cụ thể từ JsonNode
        String email = userInfoJson.get("email").asText();
        String name = userInfoJson.get("name").asText();
        return ApiResponse.<SignInResponse>builder()
                .message("Sign in successfully!")
                .result(authenticationService.authenticate(email, name))
                .build();
    }
}
