package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Date;
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
    @GeneratedValue(generator = "pond-id")
    @GenericGenerator(name = "pond-id", strategy = "com.example.demo.configuration.IdGenerator")
    int pondId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId")
    User user;

    @Column(name = "pondName", nullable = false)
    String pondName;

    @Column(name = "pumpPower")
    float pumpPower;

    @Column(name = "image", columnDefinition = "VARCHAR(MAX)")
    String image;

    @Column(name = "size")
    float size;

    @Column(name = "depth")
    float depth;

    @Column(name = "volume")
    float volume;

    @Column(name = "vein")
    int vein;

    @Column(name = "createDate")
    Date createDate;

    @OneToMany(mappedBy = "pond", cascade = CascadeType.ALL)
    List<Koi> kois;

    @OneToMany(mappedBy = "pond", cascade = CascadeType.ALL)
    List<WaterParam> waterParams;
}
