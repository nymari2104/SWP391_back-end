package com.example.demo.service;

import com.example.demo.dto.request.KoiGrowthLogCreateRequest;
import com.example.demo.entity.Koi;
import com.example.demo.entity.KoiGrowthLog;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.example.demo.mapper.KoiGrowthLogMapper;
import com.example.demo.repository.KoiGrowthLogRepository;
import com.example.demo.repository.KoiRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KoiGrowthLogService {

    KoiGrowthLogRepository koiGrowthLogRepository;
    KoiRepository koiRepository;
    KoiGrowthLogMapper koiGrowthLogMapper;

    public KoiGrowthLog createKoiGrowthLog(KoiGrowthLogCreateRequest request) {

        Koi koi = koiRepository.findById(request.getKoiId())
                .orElseThrow(() -> new AppException(ErrorCode.KOI_NOT_FOUND));

        KoiGrowthLog koiGrowthLog = new KoiGrowthLog();
        koiGrowthLogMapper.toKoiGrowthLog(koiGrowthLog, request);
        koiGrowthLog.setKoi(koi);
        return koiGrowthLogRepository.save(koiGrowthLog);
    }
}
