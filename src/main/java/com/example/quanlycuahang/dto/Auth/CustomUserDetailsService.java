package com.example.quanlycuahang.dto.Auth;

import com.example.quanlycuahang.entity.TaiKhoan.TaiKhoan;
import com.example.quanlycuahang.repository.TaiKhoan.TaiKhoanRepository;
import com.example.quanlycuahang.service.Permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private PermissionService permissionService;
    /**
     * Phương thức bắt buộc của UserDetailsService.
     * Spring Security gọi hàm này khi người dùng cố gắng đăng nhập.
     */
    @Override
    public UserDetails loadUserByUsername(String tenDangNhap) throws UsernameNotFoundException {
        TaiKhoan taiKhoan = taiKhoanRepository.findByTenDangNhap(tenDangNhap)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Không tìm thấy tài khoản: " + tenDangNhap)
                );

        // Kiểm tra trạng thái tài khoản (thêm đoạn này)
        if (taiKhoan.getTrangThai() == null || taiKhoan.getTrangThai()==false) {
            throw new UsernameNotFoundException("Tài khoản đã bị khóa hoặc vô hiệu hóa");
        }

        // Lấy danh sách Quyền từ Service
        Collection<? extends GrantedAuthority> authorities = permissionService.getAuthoritiesByRole(
                taiKhoan.getMaVaiTro(),
                taiKhoan.getIsSuperAdmin()
        );

        // Gán Quyền vào Entity (Sử dụng Setter đã thêm ở 9.2)
        taiKhoan.setAuthorities(authorities);

        return taiKhoan;
    }

}