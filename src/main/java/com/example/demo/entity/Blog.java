package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

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
    @GeneratedValue(generator = "blog-id")
    @GenericGenerator(name = "blog-id", strategy = "com.example.demo.configuration.IdGenerator")
    private int blogId;

    @JsonIgnoreProperties({"blogs"})
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    User user;

    @Column(name = "image", columnDefinition = "VARCHAR(MAX)")
    String image;

    @Column(name = "content", columnDefinition = "VARCHAR(MAX)")
    String content;

    @Column(name = "title")
    String title;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "createDate")
    Date createDate;

}
