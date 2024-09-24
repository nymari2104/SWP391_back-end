package com.example.demo.controller;

import com.example.demo.configuration.ImageResizer;
import com.example.demo.dto.request.KoiCreateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Koi;
import com.example.demo.repository.KoiRepository;
import com.example.demo.service.KoiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@RestController
@RequestMapping("/koi")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KoiController {

    KoiService koiService;

    @PostMapping("/create")
    ApiResponse<Koi> createKoi(@RequestBody KoiCreateRequest request){

//        String image = ImageResizer.resizeAndConvertImageToBase64(imageFile, 200, 200);

        return ApiResponse.<Koi>builder()
                .message("Create Koi successfully")
                .result(koiService.createKoi(KoiCreateRequest.builder()
                        .name(request.getName())
                        .image(request.getImage())
                        .sex(request.getSex())
                        .type(request.getType())
                        .origin(request.getOrigin())
                        .createDate(request.getCreateDate())
                        .pondId(request.getPondId())
                        .build()))
                .build();


    }
}
