package com.example.demo.dto.request.userRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
     String email;
     String password;
     String fullName;
     String phone;
     String address;

}
