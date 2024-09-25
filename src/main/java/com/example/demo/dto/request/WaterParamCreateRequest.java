package com.example.demo.dto.request;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WaterParamCreateRequest {

    float o2;
    float temperature;
    float nh4;
    float salt;
    float ph;
    float no2;
    float no3;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date createDate;
    int pondId;
}
