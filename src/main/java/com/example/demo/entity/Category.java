package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "categories")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Category {
    @Id
    @Column(name = "cate_id", nullable = false)
    int cateId;

    @Column(name = "cate_name", nullable = false)
    String cateName;

//    @JsonManagedReference
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Product> products;
}
