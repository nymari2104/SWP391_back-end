package com.example.demo.dto.request.pondRequest;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PondUpdateRequest {
    String pondName;
    Float pumpPower;
    String image;
    Float size;
    Float depth;
    Float volume;
    Integer vein;
}
