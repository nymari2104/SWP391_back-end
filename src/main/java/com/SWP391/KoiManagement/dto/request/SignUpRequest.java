package com.SWP391.KoiManagement.dto.request;

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
    String email;
    @Size(min = 8, message = "PASSWORD_INVALID")
     String password;
     String phone;
     String address;
}
