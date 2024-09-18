package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
    @Id
    @Column(name = "cateId", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    String cateId;

    @Column(name = "cateName", nullable = false)
     String cateName;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
     List<Product> products;
}
