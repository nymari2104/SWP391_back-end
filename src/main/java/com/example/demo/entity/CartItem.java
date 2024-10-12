package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CartItem")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartItem {
    @Id
    @Column(name = "cartItemId", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    String cartItemId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "cartId")
    Cart cart;

    @JsonIgnoreProperties({"cartItems"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "productId")
    Product product;

    @Column(name = "quantity", nullable = false)
    int quantity;
}
