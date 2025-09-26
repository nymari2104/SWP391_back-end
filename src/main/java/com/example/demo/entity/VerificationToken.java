package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "verification_tokens")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "verification_tokens_id")
    int verificationTokensId;
    
    @Column(name = "otp")
    int otp;
    
    @Column(name = "expiry_time")
    Date expiryTime;
    
    @Column(name = "email")
    String email;
    
    @Column(name = "password")
    String password;
    
    @Column(name = "fullname")
    String fullname;
}
