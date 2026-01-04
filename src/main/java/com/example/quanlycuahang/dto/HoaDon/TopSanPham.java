package com.example.quanlycuahang.dto.HoaDon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopSanPham {
    private Integer maSp;
    private String tenSp;
    private String maSku;
    private Long soDonHang;
    private Long tongSoLuong;
    private BigDecimal doanhThu;
}