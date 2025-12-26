package com.example.quanlycuahang.controller.KhachHang;

import com.example.quanlycuahang.entity.KhachHang.NhomKhachHang;
import com.example.quanlycuahang.service.KhachHang.NhomKhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/nhom-khach-hang")
public class NhomKhachHangController {

    @Autowired
    private NhomKhachHangService nhomKhachHangService;

    //api lấy danh sách hạng thành viên
    @GetMapping
    public ResponseEntity<List<NhomKhachHang>> getAllNhomKhachHang() {
        List<NhomKhachHang> nhomList = nhomKhachHangService.getAllNhomKhachHang();
        return ResponseEntity.ok(nhomList);
    }

    //api cập nhật quy tắc 1 hạng
    @PutMapping("/{ma_nhom}")
    public ResponseEntity<NhomKhachHang> updateNhomKhachHang(@PathVariable Integer ma_nhom, @RequestBody NhomKhachHang nhomKhachHang) {
        try {
            NhomKhachHang updatedNhom = nhomKhachHangService.updateNhomKhachHang(ma_nhom, nhomKhachHang);
            return ResponseEntity.ok(updatedNhom);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //api lấy chi tiết 1 hang
    @GetMapping("/{ma_nhom}")
    public ResponseEntity<NhomKhachHang> getNhomKhachHangById(@PathVariable Integer ma_nhom) {
        return nhomKhachHangService.getNhomKhachHangById(ma_nhom)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

   //api thêm mới 1 hạng thành viên
    @PostMapping
    public ResponseEntity<NhomKhachHang> createNhomKhachHang(@RequestBody NhomKhachHang nhomKhachHang) {
        NhomKhachHang createdNhom = nhomKhachHangService.createNhomKhachHang(nhomKhachHang);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNhom);
    }

    //api xóa 1 hạng thành viên
    @DeleteMapping("/{ma_nhom}")
    public ResponseEntity<Void> deleteNhomKhachHang(@PathVariable Integer ma_nhom) {
        try {
            nhomKhachHangService.deleteNhomKhachHang(ma_nhom);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}