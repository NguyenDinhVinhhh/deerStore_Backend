package com.example.quanlycuahang.controller.SanPham;

import com.example.quanlycuahang.dto.SanPham.SanPhamResponse;
import com.example.quanlycuahang.service.SanPham.SanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/san-pham")
@CrossOrigin("*")
public class SanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

    // api lấy danh sách sản phẩm
    @GetMapping
    public ResponseEntity<List<SanPhamResponse>> getAll() {
        return ResponseEntity.ok(sanPhamService.findAll());
    }


    // api lấy sản phẩm theo id
    @GetMapping("/{id}")
    public ResponseEntity<SanPhamResponse> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(sanPhamService.findByIdResponse(id));
    }

    // api thêm sản phẩm mới
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasAuthority('THEM_SAN_PHAM')")
    public ResponseEntity<?> create(
            @RequestParam("tenSP") String tenSP,
            @RequestParam("maSku") String maSku,
            @RequestParam("donGia") String donGiaStr,
            @RequestParam("giaVon") String giaVonStr,
            @RequestParam("moTa") String moTa,
            @RequestParam("maDanhMuc") String maDanhMucStr,
            @RequestPart(value = "hinhAnhFile", required = false) MultipartFile hinhAnhFile
    ) {
        BigDecimal donGia;
        BigDecimal giaVon;
        Integer maDanhMuc;

        try {
            if (maDanhMucStr == null || maDanhMucStr.trim().isEmpty() || maDanhMucStr.equalsIgnoreCase("undefined")) {
                // Đây là lỗi validation mà Frontend nên xử lý, nhưng Backend cần bắt
                return ResponseEntity
                        .badRequest()
                        .body("Mã danh mục không được để trống.");
            }
            maDanhMuc = Integer.parseInt(maDanhMucStr);
        } catch (NumberFormatException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Mã danh mục phải là một số nguyên hợp lệ.");
        }

        try {

            if (donGiaStr.isEmpty() || giaVonStr.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body("Đơn giá và Giá vốn không được để trống.");
            }
            donGia = new BigDecimal(donGiaStr);
            giaVon = new BigDecimal(giaVonStr);
        } catch (NumberFormatException e) {
            return ResponseEntity
                    .badRequest()
                    .body("Đơn giá hoặc giá vốn không hợp lệ (ví dụ: không được chứa ký tự chữ, dấu phẩy).");
        }

        SanPhamResponse newSp = sanPhamService.createByParams(
                tenSP, maSku, donGia, giaVon, moTa, maDanhMuc, hinhAnhFile
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(newSp);
    }


   //api cập nhật
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    @PreAuthorize("hasAuthority('SUA_SAN_PHAM')")
    public ResponseEntity<SanPhamResponse> update(
            @PathVariable Integer id,
            @RequestParam("tenSP") String tenSP,
            @RequestParam("maSku") String maSku,
            @RequestParam("donGia") BigDecimal donGia,
            @RequestParam("giaVon") BigDecimal giaVon,
            @RequestParam("moTa") String moTa,
            @RequestParam("maDanhMuc") Integer maDanhMuc,
            @RequestPart(value = "hinhAnhFile", required = false) MultipartFile hinhAnhFile
    ) {
        SanPhamResponse updatedSp = sanPhamService.updateByParams(
                id, tenSP, maSku, donGia, giaVon, moTa, maDanhMuc, hinhAnhFile
        );
        return ResponseEntity.ok(updatedSp);
    }

    // api xóa sản phẩm
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('XOA_SAN_PHAM')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sanPhamService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // api tìm kiếm sản phảm theo mã sku
    @GetMapping("/sku/{maSku}")
    public ResponseEntity<SanPhamResponse> getBySku(@PathVariable String maSku) {
        return ResponseEntity.ok(sanPhamService.findBySku(maSku));
    }


    // api tìm kiếm sản phẩm theo tên hoặc mã SKU (dùng cho POS / ô tìm kiếm)
    @GetMapping("/search")
    public ResponseEntity<List<SanPhamResponse>> searchSanPham(
            @RequestParam("keyword") String keyword
    ) {
        List<SanPhamResponse> result =
                sanPhamService.searchSanPhamByTenOrSku(keyword);

        return ResponseEntity.ok(result);
    }
}
