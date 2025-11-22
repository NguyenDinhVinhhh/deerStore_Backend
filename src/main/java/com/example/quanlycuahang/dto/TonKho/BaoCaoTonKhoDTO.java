package com.example.quanlycuahang.dto.TonKho;

// File: com.yourpackage.dto.BaoCaoTonKhoDTO.java

import lombok.Data;

@Data
public class BaoCaoTonKhoDTO {
    private Integer maId; // Có thể là maSp hoặc maDanhMuc
    private String ten;
    private Long tongSoLuongTon;

    public BaoCaoTonKhoDTO(Integer maId, String ten, Long tongSoLuongTon) {
        this.maId = maId;
        this.ten = ten;
        this.tongSoLuongTon = tongSoLuongTon;
    }

    // Constructor cho kết quả Object[] từ Repository
    public BaoCaoTonKhoDTO(Object[] result) {
        this.maId = (Integer) result[0];
        this.tongSoLuongTon = (Long) result[1];
        // Lưu ý: Trường 'ten' sẽ cần được bổ sung sau khi lấy tên từ Repository khác (ví dụ: DanhMucRepository/SanPhamRepository)
        this.ten = "Tên chưa được tìm kiếm";
    }
}