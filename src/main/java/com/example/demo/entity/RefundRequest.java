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
@Table(name = "refund_requests")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefundRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "refundRequestId")
    String refundRequestId;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true)
    Order order;

    @Column(name = "refund_reason", columnDefinition = "nvarchar(255)")
    String refundReason;

    @Column(name = "refund_reason_image", columnDefinition = "nvarchar(255)")
    String refundReasonImage;

    @CreationTimestamp
    @Column(name = "create_date")
    Date createDate;

    @Column(name = "status")
    String status;


    @Column(name = "admin_id")
    String adminId;
}
