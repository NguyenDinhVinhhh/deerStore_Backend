package com.example.quanlycuahang.dto.HoaDon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HoaDonResponse {
    private Integer maHoaDon;
    private LocalDateTime ngayLap;
    private BigDecimal thanhTien;
    private String trangThai;
    private Integer maChiNhanh;

    public HoaDonResponse(
            Integer maHoaDon,
            LocalDateTime ngayLap,
            BigDecimal thanhTien,
            String trangThai,
            Integer maChiNhanh
    ) {
        this.maHoaDon = maHoaDon;
        this.ngayLap = ngayLap;
        this.thanhTien = thanhTien;
        this.trangThai = trangThai;
        this.maChiNhanh = maChiNhanh;
    }

    public Integer getMaHoaDon() {
        return maHoaDon;
    }

    public LocalDateTime getNgayLap() {
        return ngayLap;
    }

    public BigDecimal getThanhTien() {
        return thanhTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public Integer getMaChiNhanh() {
        return maChiNhanh;
    }
}
