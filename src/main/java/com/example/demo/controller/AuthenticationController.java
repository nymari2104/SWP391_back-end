package com.example.demo.controller;

import com.example.demo.dto.request.authenticationRequest.*;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.authenticationResponse.SignInResponse;
import com.example.demo.dto.response.authenticationResponse.IntrospectResponse;
import com.example.demo.dto.response.userResponse.UserResponse;
import com.example.demo.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    ResponseEntity<ApiResponse<SignInResponse>> signIn(@RequestBody SignInRequest request) {
        var result = authenticationService.authenticate(request);
        return ResponseEntity.ok()
                .body(ApiResponse.<SignInResponse>builder()
                        .message("Sign in successfully!")
                        .result(result)
                        .build());
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws JOSEException, ParseException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .message("Introspect successfully!")
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    ResponseEntity<ApiResponse<Void>> logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenticationService.Logout(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify-sign-up")
    ApiResponse<UserResponse> verifySignUp(@RequestBody VerifyOtpRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("sign-up successfully!")
                .result(authenticationService.verifySignUp(request))
                .build();
    }

    @PostMapping("/verify-reset-password")
    ApiResponse<String> verifyResetPassword(@RequestBody VerifyOtpRequest request) {
        return ApiResponse.<String>builder()
                .message("Verify successfully!")
                .result(authenticationService.verifyOtp(request).getEmail())
                .build();
    }

    @PostMapping("/sign-in-by-google")
    public ApiResponse<SignInResponse> loginSuccess(@RequestBody String request) throws IOException {
        return ApiResponse.<SignInResponse>builder()
                .message("Sign in successfully!")
                .result(authenticationService.authenticate(request))
                .build();
    }
}
