package com.example.demo.service;

import com.example.demo.dto.request.koiRequest.KoiCreateRequest;
import com.example.demo.dto.request.koiRequest.KoiUpdateRequest;
import com.example.demo.entity.Koi;
import com.example.demo.entity.Pond;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.KoiMapper;
import com.example.demo.repository.KoiRepository;
import com.example.demo.repository.PondRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KoiService {

    PondRepository pondRepository;
    KoiRepository koiRepository;
    KoiMapper koiMapper;

    @PreAuthorize("hasRole('USER')")
    public Koi createKoi(KoiCreateRequest request) {

        Pond pond = pondRepository.findById(request.getPondId())
                .orElseThrow(() -> new AppException(ErrorCode.POND_NOT_FOUND));

        return koiRepository.save(Koi.builder()
                .name(request.getName())
                .image(request.getImage())
                .sex(request.getSex())
                .type(request.getType())
                .origin(request.getOrigin())
                .createDate(request.getCreateDate())
                .pond(pond)
                .build());
    }

    @PreAuthorize("hasRole('USER')")
    public Koi getKoi(int koiId) {

        Optional<Koi> koi = koiRepository.findById(koiId);

        return koi.orElse(null);
    }

    public Koi updateKoi(int koiId, KoiUpdateRequest request) {
        Koi koi = koiRepository.findById(koiId)
                .orElseThrow(() -> new AppException(ErrorCode.KOI_NOT_FOUND));

        koiMapper.updateKoi(koi, request);

        if (request.getPondId() != null) {
            Pond pond = pondRepository.findById(request.getPondId())
                    .orElseThrow(() -> new AppException(ErrorCode.POND_NOT_FOUND));
            koi.setPond(pond);
        }

        return koiRepository.save(koi);
    }

    public void deleteKoi(int koiId) {
        koiRepository.deleteById(koiId);
    }

}
