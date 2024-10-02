package com.example.demo.controller;

import com.example.demo.dto.request.pondRequest.PondCreateRequest;
import com.example.demo.dto.request.pondRequest.PondUpdateRequest;
import com.example.demo.dto.request.productRequest.ProductUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Pond;
import com.example.demo.service.PondService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
                        .createDate(new Date())
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

    @GetMapping("/{pondId}")
    ApiResponse<Pond> getPond(@PathVariable int pondId) {
        return ApiResponse.<Pond>builder()
                .message("Get pond successfully")
                .result(pondService.getPond(pondId))
                .build();
    }

    @PutMapping("/update/{pondId}")
    ApiResponse<Pond> updatePond(@PathVariable int pondId, @RequestBody PondUpdateRequest request) {
        return ApiResponse.<Pond>builder()
                .message("Update pond successfully")
                .result(pondService.updatePond(pondId, request))
                .build();
    }

    @DeleteMapping("/delete/{pondId}")
    ApiResponse<Boolean> deletePond(@PathVariable int pondId) {
        pondService.deletePond(pondId);
        return ApiResponse.<Boolean>builder()
                .message("Delete pond successfully")
                .result(true)
                .build();
    }
}
