package com.example.quanlycuahang.dto.ThongKe;

import java.math.BigDecimal;

public class DoanhThuChiNhanhDTO {
    private Integer maChiNhanh;
    private String tenChiNhanh;
    private BigDecimal doanhThu;

    public DoanhThuChiNhanhDTO(
            Integer maChiNhanh,
            String tenChiNhanh,
            BigDecimal doanhThu
    ) {
        this.maChiNhanh = maChiNhanh;
        this.tenChiNhanh = tenChiNhanh;
        this.doanhThu = doanhThu;
    }

    public Integer getMaChiNhanh() {
        return maChiNhanh;
    }

    public String getTenChiNhanh() {
        return tenChiNhanh;
    }

    public BigDecimal getDoanhThu() {
        return doanhThu;
    }
}
