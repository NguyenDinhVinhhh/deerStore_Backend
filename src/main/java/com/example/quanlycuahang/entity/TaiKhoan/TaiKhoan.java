package com.example.quanlycuahang.entity.TaiKhoan;


import com.example.quanlycuahang.entity.ChiNhanh.ChiNhanh;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "tai_khoan")
@Data
public class TaiKhoan implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_tk")
    private Integer maTk;

    @Column(name = "ten_dang_nhap", unique = true, nullable = false)
    private String tenDangNhap;

    @Column(name = "email")
    private String email;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "mat_khau", nullable = false)
    private String matKhau;

    @Column(name = "trang_thai")
    private Boolean trangThai = true;

    @Column(name = "ma_vai_tro")
    private Integer maVaiTro;

    @Column(name = "is_super_admin", nullable = false)
    private Boolean isSuperAdmin = false;

    @Column(name = "ho_ten", nullable = false)
    private String hoTen;

    @ManyToMany
    @JoinTable(
            name = "tai_khoan_chi_nhanh",
            joinColumns = @JoinColumn(name = "ma_tk"),
            inverseJoinColumns = @JoinColumn(name = "ma_chi_nhanh")
    )
    private List<ChiNhanh> chiNhanhList;

    // --- Các phương thức của UserDetails (Authority tạm thời) ---
    @Transient // Đánh dấu đây không phải cột trong DB
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Nếu authorities chưa được tải (là null)
        if (this.authorities == null) {
            // Tạm thời trả về quyền mặc định (Cơ chế Dependency Injection phức tạp)
            // **Lưu ý:** Trong môi trường thực tế, bạn sẽ cần một cách để inject PermissionService vào đây
            // hoặc dùng một CustomUserDetailsService để tải quyền VÀ TRUYỀN VÀO TaiKhoan.



            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return this.authorities;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() { return matKhau; }

    @Override
    public String getUsername() { return tenDangNhap; }

    @Override public boolean isAccountNonExpired() { return true; }

    @Override public boolean isAccountNonLocked() { return trangThai != null && trangThai; }

    @Override public boolean isCredentialsNonExpired() { return true; }

    @Override public boolean isEnabled() { return trangThai != null && trangThai; }
}