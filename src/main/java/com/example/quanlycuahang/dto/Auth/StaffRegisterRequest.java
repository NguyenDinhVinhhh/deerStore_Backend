package com.example.quanlycuahang.dto.Auth;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

import java.util.List;

@Data
public class StaffRegisterRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String tenDangNhap;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String matKhau;

    @NotBlank(message = "Họ tên không được để trống")
    private String hoTen;


    private String email;
    private String sdt;

    @NotNull(message = "Mã vai trò không được để trống")
    @Min(value = 1, message = "Mã vai trò phải lớn hơn 0")
    private Integer maVaiTro;

    @NotEmpty(message = "Tài khoản phải được phân công ít nhất một chi nhánh.")
    private List<Integer> danhSachChiNhanh;

}