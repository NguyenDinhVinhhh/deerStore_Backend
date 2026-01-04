package com.example.quanlycuahang.dto.Auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTaiKhoanRequest {

    @NotBlank(message = "Họ tên không được để trống")
    @Size(min = 2, max = 50, message = "Họ tên phải từ 2 đến 50 ký tự")
    private String hoTen;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)(\\d{9})$", message = "Số điện thoại không đúng định dạng (10 số)")
    private String sdt;

    @NotNull(message = "Trạng thái không được để trống")
    private Boolean trangThai;

    @NotNull(message = "Vui lòng chọn vai trò")
    private Integer maVaiTro;


    @Size(min = 6, message = "Mật khẩu nếu đổi phải có ít nhất 6 ký tự")
    private String matKhau;

    @NotEmpty(message = "Tài khoản phải thuộc ít nhất một chi nhánh")
    private List<Integer> danhSachChiNhanh;
}