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
@Table(name = "Users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userId;
    @Column(name = "fullname", unique = true,  columnDefinition = "NVARCHAR(255)")
    String fullname;
    @Column(name = "email", unique = true,  columnDefinition = "VARCHAR(255)")
    String email;
    String phone;
    String address;
    String password;
    boolean googleAccount;
    String role;
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
