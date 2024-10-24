package com.example.demo.dto.request.userRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateMyInfoRequest {
    String fullname;
    String phone;
    String address;
}
