package com.example.demo.controller;

import com.example.demo.configuration.ImageResizer;
import com.example.demo.dto.request.PondCreateRequest;
import com.example.demo.dto.request.ProductCreateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Pond;
import com.example.demo.entity.Product;
import com.example.demo.service.PondService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/pond")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PondController {

    PondService pondService;

    @PostMapping("/create")
    ApiResponse<Pond> createPond(
            @RequestParam("name") String name,
            @RequestParam("pumpPower") float pumpPower,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("vein") int vein,
            @RequestParam("volume") float volume,
            @RequestParam("size") float size,
            @RequestParam("depth") float depth,
            @RequestParam("createDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date createDate,
            @RequestParam("user") String userId) throws IOException {

        String resizedImageBase64 = ImageResizer.resizeAndConvertImageToBase64(imageFile, 200, 200);

        return ApiResponse.<Pond>builder()
                .result(pondService.createPond(PondCreateRequest.builder()
                        .pondName(name)
                        .image(resizedImageBase64)
                        .pumpPower(pumpPower)
                        .vein(vein)
                        .size(size)
                        .depth(depth)
                        .volume(volume)
                        .createDate(createDate)
                        .userId(userId)
                        .build()))
                .message("Create pond successfully")
                .build();
    }
    @GetMapping("/user/{userId}")
    ApiResponse<List<Pond>> getPonds(@PathVariable String userId) {
        return ApiResponse.<List<Pond>>builder()
                .result(pondService.getPonds(userId))
                .build();
    }

    @GetMapping("/list")
    ApiResponse<List<Pond>> getAllPonds() {
        return ApiResponse.<List<Pond>>builder()
                .message("Get all ponds successfully")
                .result(pondService.getAllPond())
                .build();
    }
}
