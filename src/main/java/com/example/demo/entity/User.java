package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Nationalized;

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
    @Nationalized
    @Column(name = "fullname", unique = true)
    String fullname;
    @Nationalized
    @Column(name = "email", unique = true)
    String email;
    @Column(name = "phone", columnDefinition = "VARCHAR(20)")
    String phone;
    @Nationalized
    @Column(name = "address")
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
