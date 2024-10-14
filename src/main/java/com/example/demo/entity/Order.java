package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
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
    @Column(name = "orderId")
    @GeneratedValue(strategy = GenerationType.UUID)
    String orderId;

    @Column(name = "PaymentId", nullable = false, unique = true)
    String paymentId;

    @JsonBackReference
    @ManyToOne  // Cho phép null
    @JoinColumn(name = "id")  // nullable=true để cho phép giá trị null
    User user;

    @Column(name = "name")
    String name;

    @Column(name = "phone", length = 10)
    String phone;

    @Column(name = "address")
    String address;

    @Column(name = "email")
    String email;

    @Column(name = "fullname")
    String fullname;

    @Column(name = "status")
    String status;

    @Column(name = "total")
    String total;

//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "createDate")
    Date createDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderDetail> orderDetails;
}
