package com.example.demo.service;

import com.example.demo.entity.Pond;
import com.example.demo.repository.PondRepository;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PondService {

    PondRepository pondRepository;
    UserRepository userRepository;

    public Pond createPond() {

        return null;
    }
}
