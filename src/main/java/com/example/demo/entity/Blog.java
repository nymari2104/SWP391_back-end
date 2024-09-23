package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "Blogs")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Blog {
    @Id
    @Column(name = "blogId", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String blogId;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId")
    User user;

    @Column(name = "image", columnDefinition = "VARCHAR(MAX)")
    String image;

    @Column(name = "content", columnDefinition = "VARCHAR(MAX)")
    String content;

    @Column(name = "title")
    String title;

    @Column(name = "createDate")
    Date createDate;

}
