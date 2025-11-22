package com.example.quanlycuahang.dto.ChiNhanh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChiNhanhRequest {

    @NotBlank(message = "Tên chi nhánh không được để trống")
    @Size(max = 100, message = "Tên chi nhánh không được vượt quá 100 ký tự")
    private String tenChiNhanh;

    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String diaChi;

    @Size(max = 15, message = "Số điện thoại không hợp lệ")
    private String sdt;

    @Size(max = 100, message = "Tên người quản lý không được vượt quá 100 ký tự")
    private String nguoiQuanLy;

    private Boolean trangThai;
}