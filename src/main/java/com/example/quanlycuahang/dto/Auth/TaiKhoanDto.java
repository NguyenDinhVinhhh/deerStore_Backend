package com.example.quanlycuahang.dto.Auth;


import com.example.quanlycuahang.dto.ChiNhanh.ChiNhanhDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaiKhoanDto {
    private Integer maTk;
    private String tenDangNhap;
    private String hoTen;
    private String email;
    private String sdt;
    private Boolean trangThai;
    private Integer maVaiTro;
    private Boolean isSuperAdmin;
    private List<ChiNhanhDto> chiNhanhList;
}