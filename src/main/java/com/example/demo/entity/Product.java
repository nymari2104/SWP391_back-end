package com.example.demo.entity;

import com.example.demo.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Products")
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Product {
    @Id
    @Column(name = "productId", nullable = false)
    @GeneratedValue(generator = "product-id")
    @GenericGenerator(name = "product-id", strategy = "com.example.demo.configuration.IdGenerator")
    int productId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "cateId")
    Category category;

    @Column(name = "productName", nullable = false)
    String productName;

    @Column(name = "image", columnDefinition = "TEXT")
    String image;

    @Column(name = "unitPrice", nullable = false)
    float unitPrice;

    @Min(value = 0, message = "STOCK_INVALID")
    @Column(name = "stock")
    int stock;

    @Column(name = "description")
    String description;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> cartItems;
}
