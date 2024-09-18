package com.example.demo.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest {
     String email;
    @Size(min = 8, message = "PASSWORD_INVALID")
     String password;
     String fullName;
     String phone;
     String address;
}
