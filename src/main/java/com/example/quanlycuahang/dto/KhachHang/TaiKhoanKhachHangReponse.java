package com.example.quanlycuahang.dto.KhachHang;

public class TaiKhoanKhachHangReponse {
    private Integer maKhachHang;
    private String tenDangNhap;
    private String message;

    public TaiKhoanKhachHangReponse(Integer maKhachHang, String tenDangNhap, String message) {
        this.maKhachHang = maKhachHang;
        this.tenDangNhap = tenDangNhap;
        this.message = message;
    }

    public Integer getMaKhachHang() {
        return maKhachHang;
    }
    public void setMaKhachHang(Integer maKhachHang) {
        this.maKhachHang = maKhachHang;
    }
    public String getTenDangNhap() {
        return tenDangNhap;
    }
    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
