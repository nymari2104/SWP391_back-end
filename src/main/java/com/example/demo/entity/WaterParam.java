package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "water_params")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WaterParam {
    @Id
    @Column(name = "waterParamId", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    String waterParamId;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "pond_id", unique = true)  // unique = true để đảm bảo quan hệ 1-1
    Pond pond;

    @Column(name = "o2")
    float o2;

    @Column(name = "temperature")
    float temperature;

    @Column(name = "nh4")
    float nh4;

    @Column(name = "salt")
    float salt;

    @Column(name = "ph")
    float ph;

    @Column(name = "no2")
    float no2;

    @Column(name = "no3")
    float no3;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "create_date")
    Date createDate;
}
