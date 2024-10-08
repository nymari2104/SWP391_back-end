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
@Table(name = "Kois")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Koi {
    @Id
    @Column(name = "koiId", nullable = false)
    @GeneratedValue(generator = "koi-id")
    @GenericGenerator(name = "koi-id", strategy = "com.example.demo.configuration.IdGenerator")
    int koiId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "pondId")
    Pond pond;

    @Column(name = "koiName", nullable = false)
    String name;

    @Column(name = "koiImage", columnDefinition = "TEXT")
    String image;

    @Column(name = "sex")
    Boolean sex;

    @Column(name = "type")
    String type;

    @Column(name = "origin")
    String origin;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "createDate")
    Date createDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "koi", cascade = CascadeType.ALL)
    @OrderBy("koiLogDate DESC, logTime DESC")
    List<KoiGrowthLog> koiGrowthLogs;
}
