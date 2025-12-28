package com.example.quanlycuahang.dto.LichSuMuaHang;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class LichSuMuaHangResponse {

    private Integer maHoaDon;
    private LocalDateTime ngayMua;
    private BigDecimal tongTienGoc; // Cột tong_tien trong DB
    private BigDecimal giamGia;     // Cột giam_gia trong DB
    private BigDecimal thanhTien;   // Cột thanh_tien thực tế khách trả
    private List<ChiTietHoaDonResponse> chiTiet;

    public LichSuMuaHangResponse() {
    }

    // ===== GETTERS / SETTERS =====

    public Integer getMaHoaDon() { return maHoaDon; }
    public void setMaHoaDon(Integer maHoaDon) { this.maHoaDon = maHoaDon; }

    public LocalDateTime getNgayMua() { return ngayMua; }
    public void setNgayMua(LocalDateTime ngayMua) { this.ngayMua = ngayMua; }

    public BigDecimal getTongTienGoc() { return tongTienGoc; }
    public void setTongTienGoc(BigDecimal tongTienGoc) { this.tongTienGoc = tongTienGoc; }

    public BigDecimal getGiamGia() { return giamGia; }
    public void setGiamGia(BigDecimal giamGia) { this.giamGia = giamGia; }

    public BigDecimal getThanhTien() { return thanhTien; }
    public void setThanhTien(BigDecimal thanhTien) { this.thanhTien = thanhTien; }

    public List<ChiTietHoaDonResponse> getChiTiet() { return chiTiet; }
    public void setChiTiet(List<ChiTietHoaDonResponse> chiTiet) { this.chiTiet = chiTiet; }
}