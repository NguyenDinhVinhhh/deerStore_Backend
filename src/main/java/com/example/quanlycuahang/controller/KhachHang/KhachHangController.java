package com.example.quanlycuahang.controller.KhachHang;

import com.example.quanlycuahang.dto.KhachHang.KhachHangResponse;
import com.example.quanlycuahang.entity.KhachHang.KhachHang;
import com.example.quanlycuahang.service.KhachHang.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/khach-hang")
@CrossOrigin("*")
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;

   //api tìm kiếm khách hàng theo tên hoặc sdt
    @GetMapping("/tim-kiem")
    public ResponseEntity<List<KhachHangResponse>> searchCustomers(@RequestParam String keyword) {
        List<KhachHangResponse> customers = khachHangService.searchCustomers(keyword);
        return ResponseEntity.ok(customers);
    }

    //api tạo mới 1 khách hàng
    @PostMapping
    public ResponseEntity<KhachHang> createCustomer(@RequestBody KhachHang khachHang) {
        try {
            KhachHang createdCustomer = khachHangService.createCustomer(khachHang);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("/{ma_kh}/thong-tin-giam-gia")
    public ResponseEntity<BigDecimal> getCustomerDiscountPercent(@PathVariable Integer ma_kh) {
        BigDecimal discount = khachHangService.getCustomerDiscountPercent(ma_kh);
        return ResponseEntity.ok(discount);
    }

    //api lấy danh sách tất cả khách hàng
    @GetMapping
    public ResponseEntity<List<KhachHangResponse>> getAllKhachHang() {
        List<KhachHangResponse> danhSach = khachHangService.getAllKhachHang();
        return ResponseEntity.ok(danhSach);
    }
}