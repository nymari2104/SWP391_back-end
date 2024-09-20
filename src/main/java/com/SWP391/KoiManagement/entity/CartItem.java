package com.SWP391.KoiManagement.entity;

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

    @ManyToOne
    @JoinColumn(name = "cartId")
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "productId")
    Product product;

    @Column(name = "quantity", nullable = false)
    int quantity;
}
