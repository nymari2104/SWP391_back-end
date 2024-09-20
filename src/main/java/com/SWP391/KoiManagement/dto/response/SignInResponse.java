package com.SWP391.KoiManagement.dto.response;

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
    boolean authenticated = false;
}
