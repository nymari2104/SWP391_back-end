package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
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

    @ManyToOne
    @JoinColumn(name = "pondId")
    Pond pond;

    @Column(name = "koiName", nullable = false)
    String name;

    @Column(name = "koiImage")
    String image;

//    @Column(name = "size")
//    float size;

    @Column(name = "sex")
    Boolean sex;

//    @Column(name = "weight")
//    float weight;

    @Column(name = "type")
    String type;

    @Column(name = "origin")
    String origin;

    @Column(name = "createDate")
    LocalDate createDate;

    @OneToMany(mappedBy = "koi", cascade = CascadeType.ALL)
    List<KoiGrowthLog> koiGrowthLogs;
}
