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

    // ====== 3 FIELD MỚI ======
    private Integer soManhGhep;     // Số mảnh ghép
    private Integer thoiGianGhep;   // Thời gian ghép (phút)
    private Integer doKho;          // 1-Dễ, 2-TB, 3-Khó, 4-Rất khó
}
