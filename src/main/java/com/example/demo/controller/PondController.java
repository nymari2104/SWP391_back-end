package com.example.demo.controller;

import com.example.demo.dto.request.pondRequest.PondCreateRequest;
import com.example.demo.dto.request.pondRequest.PondUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Pond;
import com.example.demo.service.PondService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pond")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PondController {
    PondService pondService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<Pond>> createPond(@RequestBody PondCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Pond>builder()
                        .result(pondService.createPond(request))
                        .message("Create pond successfully")
                        .build());
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
    ResponseEntity<Void> updatePond(@PathVariable int pondId, @RequestBody PondUpdateRequest request) {
        pondService.updatePond(pondId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{pondId}")
    ResponseEntity<Void> deletePond(@PathVariable int pondId) {
        pondService.deletePond(pondId);
        return ResponseEntity.noContent().build();
    }
}
