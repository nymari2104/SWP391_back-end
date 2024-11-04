package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "refundRequests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefundRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "refundRequestId")
    String refundRequestId;

    @OneToOne
    @JoinColumn(name = "orderId", unique = true)
    Order order;

    @Column(name = "refundReason", columnDefinition = "NVARCHAR(MAX)")
    String refundReason;

    @Column(name = "refundReasonImage", columnDefinition = "TEXT")
    String refundReasonImage;

    @CreationTimestamp
    @Column(name = "createDate")
    Date createDate;

    @Column(name = "status")
    String status;


    @Column(name = "adminId")
    String adminId;
}
