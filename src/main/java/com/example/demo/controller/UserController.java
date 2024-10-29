package com.example.demo.controller;

import com.example.demo.dto.request.authenticationRequest.SignUpRequest;
import com.example.demo.dto.request.userRequest.*;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.response.authenticationResponse.SignUpResponse;
import com.example.demo.dto.response.userResponse.UserResponse;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    //Create a user
    @PostMapping("/sign-up")
    ResponseEntity<ApiResponse<SignUpResponse>> createUser(@Valid @RequestBody SignUpRequest request) {
        userService.createUser(request);
        return ResponseEntity.accepted()
                .body(ApiResponse.<SignUpResponse>builder()
                        .message("Please check your email!")
                        .result(SignUpResponse.builder()
                                .email(request.getEmail())
                                .build())
                        .build());
    }

    //Get all users
    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .message("Get all users successfully!")
                .result(userService.getUsers())
                .build();
    }

    //Get specific user
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder()
                .message("Get user successfully!")
                .result(userService.getUser(userId))
                .build();
    }

    //get info who is login
    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .message("Get my info successfully!")
                .result(userService.getMyInfo())
                .build();
    }

    //Update my info
    @PutMapping("/update-my-info")
    ResponseEntity<ApiResponse<UserResponse>> updateMyInfo(@RequestBody UpdateMyInfoRequest request) {
        return ResponseEntity.ok().body(ApiResponse.<UserResponse>builder()
                        .result(userService.updateMyInfo(request))
                .build());
    }

    //Update user
    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("Update user successfully!")
                .result(userService.updateUser(userId, request))
                .build();
    }

    //Delete user
    @DeleteMapping("/{userId}")
    ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/forgot-password")
    ResponseEntity<ApiResponse<String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        userService.forgotPassword(request);
        return ResponseEntity.accepted()
                .body(ApiResponse.<String>builder()
                        .message("Send mail successfully!")
                        .result(request.getEmail())
                        .build());
    }

    @PutMapping("/reset-password")
    ResponseEntity<ApiResponse<Void>> resetPassword(@RequestBody ResetPasswordRequest request) {
        userService.resetPassword(request);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update-password")
    ResponseEntity<ApiResponse<Void>> updatePassword(@RequestBody UpdatePasswordRequest request) {
        userService.updateMyPassword(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/create")
    ResponseEntity<ApiResponse<UserResponse>> createAdmin(@Valid @RequestBody SignUpRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<UserResponse>builder()
                        .code("201")
                        .message("Create admin successfully!")
                        .result(userService.createAdminAccount(request))
                        .build());
    }


}
