package com.example.demo.controller;

import com.example.demo.dto.request.pondRequest.PondCreateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Pond;
import com.example.demo.service.PondService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pond")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PondController {

    PondService pondService;

    @PostMapping("/create")
    ApiResponse<Pond> createPond(@RequestBody PondCreateRequest request){

//        String resizedImageBase64 = ImageResizer.resizeAndConvertImageToBase64(imageFile, 200, 200);
        return ApiResponse.<Pond>builder()
                .result(pondService.createPond(PondCreateRequest.builder()
                        .pondName(request.getPondName())
                        .image(request.getImage())
                        .pumpPower(request.getPumpPower())
                        .vein(request.getVein())
                        .size(request.getSize())
                        .depth(request.getDepth())
                        .volume(request.getVolume())
                        .createDate(request.getCreateDate())
                        .userId(request.getUserId())
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

    @GetMapping("/{pondId}")
    ApiResponse<Pond> getPond(@PathVariable int pondId) {
        return ApiResponse.<Pond>builder()
                .message("Get pond successfully")
                .result(pondService.getPond(pondId))
                .build();
    }
}
