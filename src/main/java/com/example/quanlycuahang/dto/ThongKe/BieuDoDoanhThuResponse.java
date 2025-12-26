package com.example.quanlycuahang.dto.ThongKe;

import java.math.BigDecimal;

public class BieuDoDoanhThuResponse     {
    private String label;      // ngày hoặc tháng
    private BigDecimal value;  // doanh thu

    public BieuDoDoanhThuResponse(String label, BigDecimal value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public BigDecimal getValue() {
        return value;
    }
}
