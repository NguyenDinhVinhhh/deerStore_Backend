package com.example.quanlycuahang.dto.TonKho;

// File: com.yourpackage.dto.TonKhoAdjustmentRequest.java

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class TonKhoAdjustmentRequest {

    @NotNull
    private Integer maSp;

    @NotNull
    private Integer maKho;


    @NotNull
    private Integer soLuong;

    private String ghiChu;
}