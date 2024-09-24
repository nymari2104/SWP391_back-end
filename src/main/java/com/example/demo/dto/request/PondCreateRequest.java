package com.example.demo.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.Date;
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PondCreateRequest {
    String pondName;
    float pumpPower;
    String image;
    float size;
    float depth;
    float volume;
    int vein;
    Date createDate;
    String userId;
}
