package com.example.quanlycuahang.dto.TaiKhoanKhachHang;



import jakarta.validation.constraints.NotBlank;

// DTO dùng để hứng dữ liệu từ form Đăng nhập của Khách hàng
public class LoginRequest {

    @NotBlank(message = "Tên đăng nhập không được để trống")
    private String username;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;



    public LoginRequest() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}