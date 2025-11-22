package com.example.quanlycuahang.dto.HoaDon;


import java.math.BigDecimal;

public class InvoiceResponseItemDto {
    private Integer ma_sp;
    private String ten_sp; // Giả định có thể lấy tên sản phẩm
    private Integer so_luong;
    private BigDecimal don_gia;
    private BigDecimal thanh_tien_chi_tiet; // so_luong * don_gia

    // Getters and Setters
    public Integer getMa_sp() { return ma_sp; }
    public void setMa_sp(Integer ma_sp) { this.ma_sp = ma_sp; }
    public String getTen_sp() { return ten_sp; }
    public void setTen_sp(String ten_sp) { this.ten_sp = ten_sp; }
    public Integer getSo_luong() { return so_luong; }
    public void setSo_luong(Integer so_luong) { this.so_luong = so_luong; }
    public BigDecimal getDon_gia() { return don_gia; }
    public void setDon_gia(BigDecimal don_gia) { this.don_gia = don_gia; }
    public BigDecimal getThanh_tien_chi_tiet() { return thanh_tien_chi_tiet; }
    public void setThanh_tien_chi_tiet(BigDecimal thanh_tien_chi_tiet) { this.thanh_tien_chi_tiet = thanh_tien_chi_tiet; }
}