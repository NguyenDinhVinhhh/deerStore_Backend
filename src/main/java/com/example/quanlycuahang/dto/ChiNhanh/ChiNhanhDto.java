package com.example.quanlycuahang.dto.ChiNhanh;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChiNhanhDto {
    private Integer maChiNhanh;
    private String tenChiNhanh;

    private String diaChi;
    private String sdt;
    private String nguoiQuanLy;
    private Boolean trangThai;

}