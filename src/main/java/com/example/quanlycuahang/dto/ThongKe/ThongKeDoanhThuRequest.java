package com.example.quanlycuahang.dto.ThongKe;

import java.time.LocalDateTime;

public class ThongKeDoanhThuRequest {
    private ThoiGianThongKe kieuThoiGian;

    // dùng khi kieuThoiGian = TU_CHON
    private LocalDateTime tuNgay;
    private LocalDateTime denNgay;

    // null = tất cả chi nhánh
    private Integer maChiNhanh;

    // Getter / Setter
    public ThoiGianThongKe getKieuThoiGian() {
        return kieuThoiGian;
    }

    public void setKieuThoiGian(ThoiGianThongKe kieuThoiGian) {
        this.kieuThoiGian = kieuThoiGian;
    }

    public LocalDateTime getTuNgay() {
        return tuNgay;
    }

    public void setTuNgay(LocalDateTime tuNgay) {
        this.tuNgay = tuNgay;
    }

    public LocalDateTime getDenNgay() {
        return denNgay;
    }

    public void setDenNgay(LocalDateTime denNgay) {
        this.denNgay = denNgay;
    }

    public Integer getMaChiNhanh() {
        return maChiNhanh;
    }

    public void setMaChiNhanh(Integer maChiNhanh) {
        this.maChiNhanh = maChiNhanh;
    }
}
