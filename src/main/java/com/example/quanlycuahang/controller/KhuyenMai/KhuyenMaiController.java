package com.example.quanlycuahang.controller.KhuyenMai;

import com.example.quanlycuahang.entity.KhuyenMai.KhuyenMai;
import com.example.quanlycuahang.service.KhuyenMai.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/khuyen-mai")
public class KhuyenMaiController {

    @Autowired
    private KhuyenMaiService khuyenMaiService;

    //api thêm mới 1 voicher
    @PostMapping
    @PreAuthorize("hasAuthority('THEM_KHUYEN_MAI')")
    public ResponseEntity<KhuyenMai> createKhuyenMai(@RequestBody KhuyenMai khuyenMai) {
        try {
            KhuyenMai createdKm = khuyenMaiService.createKhuyenMai(khuyenMai);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdKm);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // api cập nhật khuyến mãi
    @PutMapping("/{ma_km}")
    @PreAuthorize("hasAuthority('SUA_KHUYEN_MAI')")
    public ResponseEntity<KhuyenMai> updateKhuyenMai(@PathVariable Integer ma_km, @RequestBody KhuyenMai khuyenMai) {
        try {
            KhuyenMai updatedKm = khuyenMaiService.updateKhuyenMai(ma_km, khuyenMai);
            return ResponseEntity.ok(updatedKm);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // api xóa khuyến mãi
    @DeleteMapping("/{ma_km}")
    @PreAuthorize("hasAuthority('XOA_KHUYEN_MAI')")
    public ResponseEntity<Void> deleteKhuyenMai(@PathVariable Integer ma_km) {
        try {
            khuyenMaiService.deleteKhuyenMai(ma_km);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //api lấy danh sách khuyến mãi
    @GetMapping
    public ResponseEntity<List<KhuyenMai>> getAllKhuyenMai() {
        List<KhuyenMai> khuyenMaiList = khuyenMaiService.getAllKhuyenMai();
        return ResponseEntity.ok(khuyenMaiList);
    }



//   // api lấy thông tin khuyến mãi theo mã đang thừa chưa dùng
//    @GetMapping("/kiem-tra-code")
//    public ResponseEntity<KhuyenMai> checkKhuyenMaiCode(@RequestParam String maCode) {
//        return khuyenMaiService.getValidKhuyenMaiByCode(maCode)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
//    }
}