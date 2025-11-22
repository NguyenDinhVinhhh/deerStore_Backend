package com.example.quanlycuahang.security;

import com.example.quanlycuahang.entity.TaiKhoan.TaiKhoan;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {

    public TaiKhoan getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof TaiKhoan) {
            return (TaiKhoan) principal;
        }
        // Trường hợp không xác thực hoặc cấu hình sai
        throw new IllegalStateException("Không tìm thấy thông tin Tài khoản trong Security Context.");
    }

    public Integer getCurrentMaTk() {
        return getCurrentUser().getMaTk();
    }

    public Boolean isCurrentUserSuperAdmin() {
        return getCurrentUser().getIsSuperAdmin();
    }
}