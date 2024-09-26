package com.example.demo.dto.response.authenticationResponse;

import com.example.demo.dto.response.userResponse.UserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignInResponse {
    String token;
    UserResponse user;
}
