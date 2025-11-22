package com.example.quanlycuahang.dto.TonKho;

// Ví dụ: package com.example.quanlycuahang.dto.HoaDon;

import java.math.BigDecimal;

// Sử dụng Record (Java 16+) hoặc Class DTO bình thường
public record InvoiceCalculationResult(
        BigDecimal tongTienGoc,
        BigDecimal giamGia,
        BigDecimal thanhTien
) {}

