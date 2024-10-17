package com.example.demo.controller;

import com.example.demo.dto.request.authenticationRequest.SignUpRequest;
import com.example.demo.dto.request.userRequest.ForgotPasswordRequest;
import com.example.demo.dto.request.userRequest.ResetPasswordRequest;
import com.example.demo.dto.request.userRequest.UpdatePasswordRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.dto.request.userRequest.UserUpdateRequest;
import com.example.demo.dto.response.authenticationResponse.SignUpResponse;
import com.example.demo.dto.response.userResponse.UserResponse;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
    ApiResponse<SignUpResponse> createUser(@Valid @RequestBody SignUpRequest request){
        return ApiResponse.<SignUpResponse>builder()
                .message("Please check your email!")
                .result(userService.createUser(request))
                .build();
    }

    //Get all users
    @GetMapping
    ApiResponse<List<UserResponse>> getUsers(){
       return ApiResponse.<List<UserResponse>>builder()
               .message("Get all users successfully!")
               .result(userService.getUsers())
               .build();
    }

    //Get specific user
    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId){
        return ApiResponse.<UserResponse>builder()
                .message("Get user successfully!")
                .result(userService.getUser(userId))
                .build();
    }

    //get info who is login
    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo(){
        return ApiResponse.<UserResponse>builder()
                .message("Get my info successfully!")
                .result(userService.getMyInfo())
                .build();
    }

    //Update my info
    @PutMapping("/update-my-info")
    ApiResponse<UserResponse> updateMyInfo(@RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Update info successfully!")
                .result(userService.updateMyInfo(request))
                .build();
    }

    //Update user
    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .message("Update user successfully!")
                .result(userService.updateUser(userId, request))
                .build();
    }

    //Delete user
    @DeleteMapping("/{userId}")
    ApiResponse<String> deleteUser(@PathVariable String userId){
        userService.deleteUser(userId);
        return  ApiResponse.<String>builder()
                .result("Delete user successfully!")
                .build();
    }

    @PostMapping("/forgot-password")
    ApiResponse<String> forgotPassword(@RequestBody ForgotPasswordRequest request){
        return ApiResponse.<String>builder()
                .message("Send mail successfully!")
                .result(userService.forgotPassword(request))
                .build();
    }

    @PostMapping("/reset-password")
    ApiResponse<Void> resetPassword(@RequestBody ResetPasswordRequest request){
        userService.resetPassword(request);
        return ApiResponse.<Void>builder()
                .message("Reset password successfully!")
                .build();
    }

    @PutMapping("/update-password")
    ApiResponse<Void> updatePassword(@RequestBody UpdatePasswordRequest request){
        userService.updateMyPassword(request);
        return ApiResponse.<Void>builder()
                .message("Update password successfully!")
                .build();
    }

    @PostMapping("/admin/create")
    ApiResponse<UserResponse> createAdmin(@Valid @RequestBody SignUpRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createAdminAccount(request))
                .build();
    }
}
