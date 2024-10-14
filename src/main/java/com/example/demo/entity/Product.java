package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
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

//    @JsonBackReference

    @JsonIgnoreProperties({"products"})
    @ManyToOne(fetch = FetchType.EAGER)
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

    @JsonIgnoreProperties({"product"})
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetails;

    @JsonIgnoreProperties({"product"})
    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<CartItem> cartItems;
}
