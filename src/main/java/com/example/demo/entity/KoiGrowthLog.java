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
@Table(name = "KoiGrowthLogs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KoiGrowthLog {
    @Id
    @Column(name = "koiLogId", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    String koiLogId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "koiId")
    Koi koi;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "koiLogDate", nullable = false)
    Date koiLogDate;

    @Column(name = "weight")
    float weight;

    @Column(name = "size")
    float size;
}
