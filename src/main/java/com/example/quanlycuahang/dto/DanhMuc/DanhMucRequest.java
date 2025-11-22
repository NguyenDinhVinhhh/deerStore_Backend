package com.example.quanlycuahang.dto.DanhMuc;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DanhMucRequest {
    @NotBlank(message = "Tên danh mục không được để trống")
    private String tenDanhMuc;

    private String moTa;

    private Boolean trangThai = true;
}