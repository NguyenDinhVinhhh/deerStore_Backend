package com.example.quanlycuahang.dto.ChiNhanh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChiNhanhDto {
    private Integer maChiNhanh;
    private String tenChiNhanh;

    private String diaChi;
    private String sdt;
    private String nguoiQuanLy;
    private Boolean trangThai;

}