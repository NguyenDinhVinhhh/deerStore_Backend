package com.example.quanlycuahang.controller.LichSuMuaHang;

import com.example.quanlycuahang.service.LichSuMuaHang.LichSuMuaHangService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lich-su-mua-hang")
@RequiredArgsConstructor
public class LichSuMuaHangController {

    private final LichSuMuaHangService lichSuMuaHangService;

    @GetMapping
    public ResponseEntity<?> lichSuMuaHang(
            @RequestParam Integer soDienThoai
    ) {
        return ResponseEntity.ok(
                lichSuMuaHangService.getLichSuMuaHang(soDienThoai)
        );
    }
}
