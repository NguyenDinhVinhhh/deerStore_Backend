package com.example.quanlycuahang.dto.LichSuMuaHang;

import java.math.BigDecimal;

public class ChiTietHoaDonResponse {

    private String tenSanPham;
    private Integer soLuong;
    private BigDecimal donGia;
    private BigDecimal thanhTien;

    public ChiTietHoaDonResponse() {
    }

    public ChiTietHoaDonResponse(
            String tenSanPham,
            Integer soLuong,
            BigDecimal donGia,
            BigDecimal thanhTien
    ) {
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.donGia = donGia;
        this.thanhTien = thanhTien;
    }


    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }

    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(BigDecimal thanhTien) {
        this.thanhTien = thanhTien;
    }
}
