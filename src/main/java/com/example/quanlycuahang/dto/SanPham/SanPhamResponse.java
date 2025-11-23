package com.example.quanlycuahang.dto.SanPham;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class SanPhamResponse {
    private Integer maSp;
    private String maSku;
    private String tenSp;
    private String moTa;
    private BigDecimal donGia;
    private BigDecimal giaVon;
    private String hinhAnhUrl;
    private Integer maDanhMuc;
    private String tenDanhMuc;
    private Boolean trangThai;
    private Integer tonKhoTong;
}
