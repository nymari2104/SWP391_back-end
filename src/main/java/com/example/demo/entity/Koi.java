package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "kois")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Koi {
    @Id
    @Column(name = "koiId", nullable = false)
    @GeneratedValue(generator = "koi-id")
    @GenericGenerator(name = "koi-id", strategy = "com.example.demo.configuration.IdGenerator")
    int koiId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "pond_id")
    Pond pond;

    @Column(name = "koi_name", nullable = false)
    String name;

    @Column(name = "koi_image", columnDefinition = "TEXT")
    String image;

    @Column(name = "sex")
    Boolean sex;

    @Column(name = "type")
    String type;

    @Column(name = "origin")
    String origin;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "create_date")
    Date createDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "koi", cascade = CascadeType.ALL)
    @OrderBy("koiLogDate ASC, logTime ASC")
    List<KoiGrowthLog> koiGrowthLogs;
}
