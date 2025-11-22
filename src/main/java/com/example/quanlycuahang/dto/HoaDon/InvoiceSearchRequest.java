package com.example.quanlycuahang.dto.HoaDon;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceSearchRequest {


    private Integer ma_tk;
    private Integer ma_chi_nhanh;
    private String trang_thai;
    private LocalDateTime tu_ngay;
    private LocalDateTime den_ngay;


    private int page = 0;
    private int size = 20;
    private String sortBy = "ngay_lap";
    private String sortDirection = "DESC";
}