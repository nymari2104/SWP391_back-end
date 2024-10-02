package com.example.demo.dto.request.koiRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiUpdateRequest {
    String name;
    String image;
    Boolean sex;
    String type;
    String origin;
    Integer pondId;
}
