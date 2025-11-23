package com.example.quanlycuahang.repository.KhachHang;

import com.example.quanlycuahang.entity.TaiKhoanKhachHang.TaiKhoanKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TaiKhoanKhachHangRepository extends JpaRepository<TaiKhoanKhachHang, Integer> {
    Optional<TaiKhoanKhachHang> findByTenDangNhap(String tenDangNhap);
}