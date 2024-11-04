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
@Table(name = "OrderDetails")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "orderDetailId")
    String orderDetailId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "orderId")
    Order order;

    @JsonIgnoreProperties({"orderDetails"})
    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    Product product;

    @Column(name = "productName", columnDefinition = "NVARCHAR(MAX)")
    String productName;

    float unitPrice;

    @Column(name = "description", columnDefinition = "TEXT")
    String description;

    @Column(name = "total", nullable = false)
    float total;

    @Column(name = "quantity", nullable = false)
    int quantity;

    public void snapshotProduct(Product product) {
        this.productName = product.getProductName();
        this.unitPrice = product.getUnitPrice();
        this.description = product.getDescription();
    }

}


