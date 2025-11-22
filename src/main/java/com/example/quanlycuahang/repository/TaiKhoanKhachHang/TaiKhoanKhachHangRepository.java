package com.example.quanlycuahang.repository.TaiKhoanKhachHang;



import com.example.quanlycuahang.entity.TaiKhoanKhachHang.TaiKhoanKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaiKhoanKhachHangRepository extends JpaRepository<TaiKhoanKhachHang, Integer> {

    /**
     * Tìm kiếm tài khoản khách hàng bằng tên đăng nhập (dùng cho Login).
     *
     * @param tenDangNhap Tên đăng nhập (thường là SDT hoặc Email).
     * @return Optional chứa TaiKhoanKhachHang nếu tìm thấy.
     */
    Optional<TaiKhoanKhachHang> findByTenDangNhap(String tenDangNhap);

    // Kế thừa các phương thức cơ bản khác như findById (dùng maKh), save, delete...
}