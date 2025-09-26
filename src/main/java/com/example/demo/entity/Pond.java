package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ponds")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Pond {
    @Id
    @Column(name = "pondId", nullable = false)
    @GeneratedValue(generator = "pond-id")
    @GenericGenerator(name = "pond-id", strategy = "com.example.demo.configuration.IdGenerator")
    int pondId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "pond_name", nullable = false)
    String pondName;

    @Column(name = "pump_power")
    float pumpPower;

    @Column(name = "image", columnDefinition = "TEXT")
    String image;

    @Column(name = "size")
    float size;

    @Column(name = "depth")
    float depth;

    @Column(name = "volume")
    float volume;

    @Column(name = "vein")
    int vein;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "create_date")
    Date createDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "pond", cascade = CascadeType.ALL)
    List<Koi> kois;

    @JsonManagedReference
    @OneToOne(mappedBy = "pond", cascade = CascadeType.ALL)
    WaterParam waterParam;
}
