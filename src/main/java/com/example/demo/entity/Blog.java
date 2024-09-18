package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Blogs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Blog {
    @Id
    @Column(name = "blogId", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String blogId;

    @ManyToOne
    @JoinColumn(name = "userId")
     User user;

    @Column(name = "content")
     String content;

    @Column(name = "title")
     String title;

    @Column(name = "createDate")
     LocalDate createDate;

}
