package com.example.demo.configuration;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    //Auto create an account Admin
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            if (userRepository.findByEmail("admin1@gmail.com").isEmpty()){

                User user = User.builder()
                        .email("admin1@gmail.com")
                        .fullname("Admin01")
                        .role(Role.ADMIN.toString())
                        .password(passwordEncoder.encode("123456789"))
                        .build();

                userRepository.save(user);
                log.warn("admin user has been create with default password: 123456789, please change it!");
            }
        };
    }
}
