package com.example.quanlycuahang.dto.ThongKe;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DoanhThuTheoThoiGianResponse {
    private LocalDate ngay;
    private BigDecimal doanhThu;

    public DoanhThuTheoThoiGianResponse(LocalDate ngay, BigDecimal doanhThu) {
        this.ngay = ngay;
        this.doanhThu = doanhThu;
    }

    public LocalDate getNgay() {
        return ngay;
    }

    public BigDecimal getDoanhThu() {
        return doanhThu;
    }
}
