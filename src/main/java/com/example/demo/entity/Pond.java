package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Ponds")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pond {
    @Id
    @Column(name = "pondId", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    String pondId;

    @ManyToOne
    @JoinColumn(name = "userId")
     User user;

    @Column(name = "pondName", nullable = false)
     String pondName;

    @Column(name = "pumpPower")
     float pumpPower;

    @Lob
    @Column(name = "image")
     byte[] image;

    @Column(name = "size")
     float size;

    @Column(name = "depth")
     float depth;

    @Column(name = "volume")
     float volume;

    @Column(name = "vein")
    private int vein;

    @Column(name = "createDate")
    private LocalDate createDate;

    @OneToMany(mappedBy = "pond", cascade = CascadeType.ALL)
    private List<Koi> kois;

    @OneToMany(mappedBy = "pond", cascade = CascadeType.ALL)
    private List<WaterParam> waterParams;
}
