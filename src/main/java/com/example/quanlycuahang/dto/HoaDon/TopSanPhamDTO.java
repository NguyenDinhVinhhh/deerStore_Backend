package com.example.quanlycuahang.dto.HoaDon;

import java.math.BigDecimal;

public class TopSanPhamDTO {
    private Integer maSp;
    private String tenSp;
    private String maSku;
    private Long tongDon;
    private Long tongSoLuong;
    private BigDecimal tongDoanhThu;

    public TopSanPhamDTO(
            Integer maSp,
            String tenSp,
            String maSku,
            Long tongDon,
            Long tongSoLuong,
            BigDecimal tongDoanhThu
    ) {
        this.maSp = maSp;
        this.tenSp = tenSp;
        this.maSku = maSku;
        this.tongDon = tongDon;
        this.tongSoLuong = tongSoLuong;
        this.tongDoanhThu = tongDoanhThu;
    }

    public Integer getMaSp() { return maSp; }
    public String getTenSp() { return tenSp; }
    public String getMaSku() { return maSku; }
    public Long getTongDon() { return tongDon; }
    public Long getTongSoLuong() { return tongSoLuong; }
    public BigDecimal getTongDoanhThu() { return tongDoanhThu; }
}
