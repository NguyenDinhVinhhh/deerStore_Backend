package com.example.quanlycuahang.dto.SanPham;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SanPhamRequest {

    // Yêu cầu bắt buộc phải có khi thêm sản phẩm
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String tenSp;

    @NotBlank(message = "Mã SKU không được để trống")
    private String maSku;

    @NotNull(message = "Đơn giá không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Đơn giá phải lớn hơn 0")
    private BigDecimal donGia;

    @NotNull(message = "Giá vốn không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá vốn không thể âm")
    private BigDecimal giaVon;

    @NotNull(message = "Mã danh mục không được để trống")
    @Min(value = 1, message = "Mã danh mục không hợp lệ")
    private Integer maDanhMuc;

    private String moTa;

    private Boolean trangThai = true;



}