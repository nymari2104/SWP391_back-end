package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "invalidated_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvalidatedToken {
    @Id
    @Column(name = "token_id")
    String tokenId;
    
    @Column(name = "expiry_date")
    Date expiryDate;
}
