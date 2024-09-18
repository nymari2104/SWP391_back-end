package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Products")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product {
    @Id
    @Column(name = "productId", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    String productId;

    @ManyToOne
    @JoinColumn(name = "cateId")
    Category category;

    @Column(name = "productName", nullable = false)
    String productName;

    @Lob
    @Column(name = "image")
    byte[] image;

    @Column(name = "unitPrice", nullable = false)
    float unitPrice;

    @Column(name = "stock")
    int stock;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product")
    private List<CartItem> cartItems;
}
