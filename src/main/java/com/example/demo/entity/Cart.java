package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "carts")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Cart {
    @Id
    @Column(name = "cartId", nullable = false)
     String cartId;

    @OneToOne
    @JoinColumn(name = "userId")
     User user;

    @Column(name = "createDate")
     LocalDate createDate;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
     List<CartItem> cartItems;
}
