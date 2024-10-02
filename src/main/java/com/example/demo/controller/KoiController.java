package com.example.demo.controller;

import com.example.demo.dto.request.koiRequest.KoiCreateRequest;
import com.example.demo.dto.request.koiRequest.KoiUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Koi;
import com.example.demo.service.KoiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/koi")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KoiController {

    KoiService koiService;

    @PostMapping("/create")
    ApiResponse<Koi> createKoi(@RequestBody KoiCreateRequest request) {

//        String image = ImageResizer.resizeAndConvertImageToBase64(imageFile, 200, 200);

        return ApiResponse.<Koi>builder()
                .message("Create Koi successfully")
                .result(koiService.createKoi(KoiCreateRequest.builder()
                        .name(request.getName())
                        .image(request.getImage())
                        .sex(request.getSex())
                        .type(request.getType())
                        .origin(request.getOrigin())
                        .createDate(new Date())
                        .pondId(request.getPondId())
                        .build()))
                .build();
    }
    @GetMapping("/{koiId}")
    ApiResponse<Koi> getKoi(@PathVariable int koiId) {
        return ApiResponse.<Koi>builder()
                .message("Get koi successfully")
                .result(koiService.getKoi(koiId))
                .build();
    }

    @PutMapping("/update/{koiId}")
    ApiResponse<Koi> updateKoi(@PathVariable int koiId, @RequestBody KoiUpdateRequest request) {
        return ApiResponse.<Koi>builder()
                .message("Update koi successfully")
                .result(koiService.updateKoi(koiId, request))
                .build();
    }

    @DeleteMapping("/delete/{koiId}")
    ApiResponse<Boolean> deletePond(@PathVariable int koiId) {
        koiService.deleteKoi(koiId);
        return ApiResponse.<Boolean>builder()
                .message("Delete pond successfully")
                .result(true)
                .build();
    }
}
