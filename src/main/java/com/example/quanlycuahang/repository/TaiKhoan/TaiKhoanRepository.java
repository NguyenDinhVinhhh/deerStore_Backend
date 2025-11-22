package com.example.quanlycuahang.repository.TaiKhoan;



import com.example.quanlycuahang.entity.TaiKhoan.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Integer> {

    Optional<TaiKhoan> findByTenDangNhap(String tenDangNhap);

    boolean existsByTenDangNhap(String tenDangNhap);
    List<TaiKhoan> findByHoTenContainingIgnoreCase(String hoTen);
}