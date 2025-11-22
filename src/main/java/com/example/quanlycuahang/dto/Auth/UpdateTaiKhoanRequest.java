package com.example.quanlycuahang.dto.Auth;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaiKhoanRequest {
    private String hoTen;
    private String email;
    private String sdt;
    private Boolean trangThai;
    private Integer maVaiTro;
    private String matKhau;

    private List<Integer> danhSachChiNhanh;
}