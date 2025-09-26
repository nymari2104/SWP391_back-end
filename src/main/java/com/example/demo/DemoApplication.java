package com.example.demo;

import com.example.demo.entity.Category;
import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Slf4j
@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!categoryRepository.existsById(1)) {
            categoryRepository.save(Category.builder()
                    .cateId(1)
                    .cateName("Koi Health Treatment")
                    .build());
        }
        if (!categoryRepository.existsById(2)) {
            categoryRepository.save(Category.builder()
                    .cateId(2)
                    .cateName("Water Parameter Improvement")
                    .build());
        }
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            userRepository.save(User.builder()
                    .email("admin@gmail.com")
                    .fullname("admin")
                    .phone("0562322508")
                            .password(passwordEncoder.encode("12345678"))
                    .role(Role.ADMIN.name()).build());
            log.info("Admin successfully logged in");
        }

    }
}
