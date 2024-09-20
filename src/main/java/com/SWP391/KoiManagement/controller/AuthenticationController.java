package com.SWP391.KoiManagement.controller;

import com.SWP391.KoiManagement.dto.request.LogoutRequest;
import com.SWP391.KoiManagement.dto.response.ApiResponse;
import com.SWP391.KoiManagement.dto.request.SignInRequest;
import com.SWP391.KoiManagement.dto.request.IntrospectRequest;
import com.SWP391.KoiManagement.dto.response.SignInResponse;
import com.SWP391.KoiManagement.dto.response.IntrospectResponse;
import com.SWP391.KoiManagement.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
}
