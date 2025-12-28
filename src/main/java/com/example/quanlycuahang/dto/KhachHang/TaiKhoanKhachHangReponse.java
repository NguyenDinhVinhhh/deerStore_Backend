package com.example.quanlycuahang.dto.KhachHang;

public class TaiKhoanKhachHangReponse {
    private Integer maKhachHang;
    private String tenDangNhap;
    private String token;
    private String message;

    public TaiKhoanKhachHangReponse(
            Integer maKhachHang,
            String tenDangNhap,
            String token,
            String message
    ) {
        this.maKhachHang = maKhachHang;
        this.tenDangNhap = tenDangNhap;
        this.token = token;
        this.message = message;
    }

    public Integer getMaKhachHang() {
        return maKhachHang;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }
}
