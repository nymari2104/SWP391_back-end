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
    float pumpPower;
    String image;
    float size;
    float depth;
    float volume;
    int vein;
}
