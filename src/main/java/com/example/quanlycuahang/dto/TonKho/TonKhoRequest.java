package com.example.quanlycuahang.dto.TonKho;

// File: com.yourpackage.dto.TonKhoRequest.java

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class TonKhoRequest {

    @NotNull(message = "Mã sản phẩm không được để trống")
    private Integer maSp;

    @NotNull(message = "Mã kho không được để trống")
    private Integer maKho;

    @NotNull(message = "Số lượng tồn không được để trống")
    private Integer soLuongTon;

    private String ghiChu;
}