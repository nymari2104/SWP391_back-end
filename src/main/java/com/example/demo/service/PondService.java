package com.example.demo.service;

import com.example.demo.dto.request.pondRequest.PondCreateRequest;
import com.example.demo.entity.Pond;
import com.example.demo.entity.User;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.repository.PondRepository;
import com.example.demo.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PondService {

    PondRepository pondRepository;
    UserRepository userRepository;

    @PreAuthorize("hasRole('USER')")
    public Pond createPond(PondCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return pondRepository.save(
                Pond.builder()
                        .pondName(request.getPondName())
                        .image(request.getImage())
                        .depth(request.getDepth())
                        .pumpPower(request.getPumpPower())
                        .size(request.getSize())
                        .vein(request.getVein())
                        .volume(request.getVolume())
                        .createDate(request.getCreateDate())
                        .user(user)
                        .build()
        );
    }

    public Pond getPond(int pondId) {
        return pondRepository.findById(pondId)
                .orElseThrow(() -> new AppException(ErrorCode.POND_NOT_FOUND));
    }

    @PreAuthorize("hasRole('USER')")
    public List<Pond> getPonds(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return user.getPonds();
    }

    public List<Pond> getAllPond() {
        return pondRepository.findAll().stream().toList();
    }

}
