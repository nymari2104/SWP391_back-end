package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "koi_growth_logs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiGrowthLog {
    @Id
    @Column(name = "koiLogId", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    String koiLogId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "koi_id")
    Koi koi;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "koi_log_date", nullable = false)
    Date koiLogDate;

    @Column(name = "log_time", nullable = false)
    @DateTimeFormat(pattern = "HH:mm:ss")
    java.sql.Time logTime;  // Cột lưu giờ

    @Column(name = "weight")
    float weight;

    @Column(name = "size")
    float size;
}
