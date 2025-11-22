package com.example.quanlycuahang.dto.Auth;

import com.example.quanlycuahang.dto.ChiNhanh.ChiNhanhDto;
import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AuthResponse {

    private String token;
    private String hoTen;
    private Boolean isSuperAdmin;
    private Integer maTk;
    private Integer maVaiTro;

    private List<ChiNhanhDto> chiNhanhList;
}