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
@Table(name = "VerificationTokens")
@FieldDefaults(level = AccessLevel.PRIVATE)

public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    int verificationTokensId;
    int otp;
    Date expiryTime;
    String email;
    String password;
    String fullname;
}
