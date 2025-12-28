package com.example.quanlycuahang.dto.LichSuMuaHang;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class LichSuMuaHangResponse {

    private Integer maHoaDon;
    private LocalDateTime ngayMua;
    private BigDecimal tongTien;
    private List<ChiTietHoaDonResponse> chiTiet;

    public LichSuMuaHangResponse() {
    }

    public LichSuMuaHangResponse(
            Integer maHoaDon,
            LocalDateTime ngayMua,
            BigDecimal tongTien,
            List<ChiTietHoaDonResponse> chiTiet
    ) {
        this.maHoaDon = maHoaDon;
        this.ngayMua = ngayMua;
        this.tongTien = tongTien;
        this.chiTiet = chiTiet;
    }

    // ===== GETTERS / SETTERS =====

    public Integer getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(Integer maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public LocalDateTime getNgayMua() {
        return ngayMua;
    }

    public void setNgayMua(LocalDateTime ngayMua) {
        this.ngayMua = ngayMua;
    }

    public BigDecimal getTongTien() {
        return tongTien;
    }

    public void setTongTien(BigDecimal tongTien) {
        this.tongTien = tongTien;
    }

    public List<ChiTietHoaDonResponse> getChiTiet() {
        return chiTiet;
    }

    public void setChiTiet(List<ChiTietHoaDonResponse> chiTiet) {
        this.chiTiet = chiTiet;
    }
}
