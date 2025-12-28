package com.example.quanlycuahang.security;

import com.example.quanlycuahang.entity.TaiKhoan.TaiKhoan;
import com.example.quanlycuahang.entity.TaiKhoanKhachHang.TaiKhoanKhachHang;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {

    public Object getCurrentPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    // Hàm lấy ID khách hàng đang đăng nhập
    public Integer getCurrentMaKhachHang() {
        Object principal = getCurrentPrincipal();
        if (principal instanceof TaiKhoanKhachHang) {
            return ((TaiKhoanKhachHang) principal).getMaKh();
        }
        throw new IllegalStateException("Người dùng hiện tại không phải là Khách hàng.");
    }

    // Hàm lấy ID nhân viên đang đăng nhập
    public Integer getCurrentMaNhanVien() {
        Object principal = getCurrentPrincipal();
        if (principal instanceof TaiKhoan) {
            return ((TaiKhoan) principal).getMaTk();
        }
        throw new IllegalStateException("Người dùng hiện tại không phải là Nhân viên.");
    }
}