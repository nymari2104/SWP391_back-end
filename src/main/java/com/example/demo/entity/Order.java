package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    @Id
    @Column(name = "orderId")
    @GeneratedValue(strategy = GenerationType.UUID)
    String orderId;

    @Column(name = "payment_id", nullable = false, unique = true)
    String paymentId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "phone", columnDefinition = "VARCHAR(20)")
    String phone;

    @Column(name = "address", columnDefinition = "TEXT")
    String address;

    @Column(name = "email", columnDefinition = "VARCHAR(255)")
    String email;

    @Column(name = "fullname", columnDefinition = "TEXT")
    String fullname;

    @Column(name = "status")
    String status;

    @Column(name = "total")
    float total;

//    @Temporal(TemporalType.DATE)
//    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "create_date")
    Date createDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<OrderDetail> orderDetails;

    @OneToOne(mappedBy = "order")
    RefundRequest refundRequest;
}
