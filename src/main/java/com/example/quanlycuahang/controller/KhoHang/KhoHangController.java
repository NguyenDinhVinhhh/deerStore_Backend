package com.example.quanlycuahang.controller.KhoHang;

import com.example.quanlycuahang.entity.KhoHang.KhoHang;
import com.example.quanlycuahang.service.KhoHang.KhoHangService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/kho-hang")
@CrossOrigin("*")
public class KhoHangController {

    private final KhoHangService khoHangService;

    public KhoHangController(KhoHangService khoHangService) {
        this.khoHangService = khoHangService;
    }

    //  api lấy tất cả kho hàng
    @GetMapping
    public ResponseEntity<List<KhoHang>> getAllWarehouses() {
        List<KhoHang> warehouses = khoHangService.findAll();
        return ResponseEntity.ok(warehouses);
    }

    // api lấy kho hàng theo mã chi nhánh
    @GetMapping("/chi-nhanh/{maChiNhanh}")
    public ResponseEntity<List<KhoHang>> getKhoHangByMaChiNhanh(@PathVariable Integer maChiNhanh) {
        List<KhoHang> khoHangs = khoHangService.findByMaChiNhanh(maChiNhanh);

        if (khoHangs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(khoHangs);
    }




    @PostMapping
    public ResponseEntity<KhoHang> createWarehouse(@RequestBody KhoHang khoHang) {
        KhoHang savedKhoHang = khoHangService.save(khoHang);
        return new ResponseEntity<>(savedKhoHang, HttpStatus.CREATED);
    }


    @PutMapping("/{maKho}")
    public ResponseEntity<KhoHang> updateWarehouse(@PathVariable Integer maKho, @RequestBody KhoHang khoHangDetails) {
        return khoHangService.findById(maKho)
                .map(existingKhoHang -> {
                    // Đảm bảo cập nhật các trường cần thiết
                    existingKhoHang.setTenKho(khoHangDetails.getTenKho());
                    existingKhoHang.setDiaChiKho(khoHangDetails.getDiaChiKho());
                    existingKhoHang.setMaChiNhanh(khoHangDetails.getMaChiNhanh());
                    existingKhoHang.setMoTa(khoHangDetails.getMoTa());

                    return ResponseEntity.ok(khoHangService.save(existingKhoHang));
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{maKho}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Integer maKho) {
        if (!khoHangService.findById(maKho).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        khoHangService.deleteById(maKho);
        return ResponseEntity.noContent().build();
    }
}