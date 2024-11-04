package com.example.demo.controller;

import com.example.demo.dto.request.koiRequest.KoiCreateRequest;
import com.example.demo.dto.request.koiRequest.KoiUpdateRequest;
import com.example.demo.dto.response.ApiResponse;
import com.example.demo.entity.Koi;
import com.example.demo.service.KoiService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/koi")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class KoiController {

    KoiService koiService;

    @PostMapping("/create")
    ResponseEntity<ApiResponse<Koi>> createKoi(@RequestBody KoiCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<Koi>builder()
                        .message("Create Koi successfully")
                        .result(koiService.createKoi(request))
                        .build());
    }

    @GetMapping("/{koiId}")
    ApiResponse<Koi> getKoi(@PathVariable int koiId) {
        return ApiResponse.<Koi>builder()
                .message("Get koi successfully")
                .result(koiService.getKoi(koiId))
                .build();
    }

    @PutMapping("/update/{koiId}")
   ResponseEntity<ApiResponse<Koi>> updateKoi(@PathVariable int koiId, @RequestBody KoiUpdateRequest request) {
        return ResponseEntity.ok().body(ApiResponse.<Koi>builder()
                        .message("Update koi successfully")
                        .result(koiService.updateKoi(koiId, request))
                .build());
    }

    @DeleteMapping("/delete/{koiId}")
    ResponseEntity<Void> deletePond(@PathVariable int koiId) {
        koiService.deleteKoi(koiId);
        return ResponseEntity.noContent().build();
    }
}
