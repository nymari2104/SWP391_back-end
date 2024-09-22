package com.example.demo.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest {
    String fullname;

    @NotBlank(message = "email cannot be blank")
    @Email(message = "EMAIL_INVALID")
    @Size(min = 3, message = "EMAIL_INVALID")
    String email;
    @Size(min = 8, message = "PASSWORD_INVALID")
     String password;
}
