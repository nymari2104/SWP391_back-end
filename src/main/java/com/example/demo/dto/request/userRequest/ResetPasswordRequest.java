package com.example.demo.dto.request.userRequest;

import com.example.demo.dto.request.authenticationRequest.VerifyOtpRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResetPasswordRequest {
    VerifyOtpRequest verifyOtpRequest;
    UserUpdateRequest userUpdateRequest;
}
