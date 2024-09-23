package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @ManyToOne
    @JoinColumn(name = "koiId")
    Koi koi;

    @Column(name = "koiLogDate", nullable = false)
    Date koiLogDate;

    @Column(name = "weight")
    float weight;

    @Column(name = "size")
    float size;
}
