package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
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
    @Column(name = "orderId", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    String orderId;

    @JsonBackReference
    @ManyToOne(optional = true)  // Cho phép null
    @JoinColumn(name = "id", nullable = true)  // nullable=true để cho phép giá trị null
    User user;

    @Column(name = "name")
    String name;

    @Column(name = "phone", length = 10)
    String phone;

    @Column(name = "address")
    String address;

    @Column(name = "email")
    String email;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "createDate")
    Date createDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderDetail> orderDetails;
}
