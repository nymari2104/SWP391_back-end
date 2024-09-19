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
@Table(name = "Orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @Column(name = "orderId", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    String orderId;

    @ManyToOne
    @JoinColumn(name = "id")
     User user;

    @Column(name = "orderDate")
     LocalDate orderDate;

    @Column(name = "phone", length = 20)
     String phone;

    @Column(name = "address")
     String address;

    @Column(name = "email")
     String email;

    @Column(name = "createDate")
     LocalDate createDate;

    @Column(name = "status")
    boolean status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
     List<OrderDetail> orderDetails;
}
