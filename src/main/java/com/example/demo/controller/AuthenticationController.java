package com.example.demo.controller;

import com.example.demo.dto.request.authenticationRequest.*;
import com.example.demo.dto.request.userRequest.ResetPasswordRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.authenticationResponse.SignInResponse;
import com.example.demo.dto.response.authenticationResponse.IntrospectResponse;
import com.example.demo.dto.response.userResponse.UserResponse;
import com.example.demo.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

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
            throws ParseException, JOSEException {
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
    ApiResponse<Void> verifyResetPassword(@RequestBody ResetPasswordRequest request){
        authenticationService.verifyResetPassword(request);
        return ApiResponse.<Void>builder()
                .message("Reset password successfully!")
                .build();
    }

    @GetMapping("/secured")
    public String secure(){
        return "Hello, secured!";
    }

    @GetMapping("/sign-in-by-google")
    public ApiResponse<SignInResponse> loginSuccess(@AuthenticationPrincipal OAuth2User oAuth2User){
        String email = oAuth2User.getAttribute("email");
        String fullname = oAuth2User.getAttribute("name");
        return ApiResponse.<SignInResponse>builder()
                .message("Sign in successfully!")
                .result(authenticationService.authenticate(email, fullname))
                .build();
    }
}
