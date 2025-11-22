package com.example.quanlycuahang.security;

import com.example.quanlycuahang.entity.ChiNhanh.TaiKhoanChiNhanh;
import com.example.quanlycuahang.repository.ChiNhanh.TaiKhoanChiNhanhRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BranchAuthorizationService {

    @Autowired
    private TaiKhoanChiNhanhRepository taiKhoanChiNhanhRepository;


    /**
     * Kiểm tra xem Tài khoản (maTk) có được gán cho Chi nhánh (maChiNhanh) này không.
     * Áp dụng ngoại lệ cho Super Admin.
     */
    public void authorizeAccessToBranch(Integer maTk, Integer maChiNhanh, boolean isSuperAdmin) {

        // 1. Ngoại lệ cho Super Admin: Super Admin có thể truy cập mọi Chi nhánh
        if (isSuperAdmin) {
            return;
        }

        // 2. Kiểm tra trong bảng liên kết TaiKhoanChiNhanh
        TaiKhoanChiNhanh.TaiKhoanChiNhanhId id = new TaiKhoanChiNhanh.TaiKhoanChiNhanhId();
        id.setMaTk(maTk);
        id.setMaChiNhanh(maChiNhanh);

        boolean isAssigned = taiKhoanChiNhanhRepository.existsById(id);

        if (!isAssigned) {
            // Ném ra ngoại lệ 403 (Access Denied) nếu không được gán
            throw new AccessDeniedException("Bạn không có quyền truy cập dữ liệu của Chi nhánh #" + maChiNhanh);
        }
    }
}
