package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity//mark as a table
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userId;
    @Column(name = "fullname", unique = true, columnDefinition = "nvarchar(255)")
    String fullname;
    @Column(name = "email", unique = true, columnDefinition = "nvarchar(255)")
    String email;
    @Column(name = "phone", columnDefinition = "VARCHAR(20)")
    String phone;
    @Column(name = "address", columnDefinition = "nvarchar(255)")
    String address;
    @Column(name = "password")
    String password;
    @Column(name = "google_account")
    boolean googleAccount;
    @Column(name = "role")
    String role;
    @Column(name = "status")
    boolean status;

//    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Blog> blogs;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonManagedReference
    Cart cart;

    @OneToMany(mappedBy = "user")
    List<Order> orders;

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Pond> ponds;

    @PrePersist
    protected void onCreate(){
        if (!this.status)
            this.status = true;
    }
}
