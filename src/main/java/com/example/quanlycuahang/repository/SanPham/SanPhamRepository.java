package com.example.quanlycuahang.repository.SanPham;

import com.example.quanlycuahang.entity.SanPham.SanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // 👈 Cần import
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    // 🎯 PHƯƠNG THỨC FIX LỖI CACHE/MAPPING
    // Sử dụng JPQL để đảm bảo toàn bộ Entity được tải (không bị lỗi Projection)
    @Query("SELECT p FROM SanPham p WHERE p.maSp IN :maSpList")
    List<SanPham> findFullProductsByIds(List<Integer> maSpList); // 👈 Thêm hàm này

    // Tìm kiếm sản phẩm theo Mã SKU (cần kiểm tra trùng lặp khi thêm mới)
    Optional<SanPham> findByMaSku(String maSku);

    // Tìm kiếm sản phẩm theo danh mục
    List<SanPham> findByMaDanhMuc(Integer maDanhMuc);
}