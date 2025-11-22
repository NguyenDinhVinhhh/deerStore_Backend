package com.example.quanlycuahang.entity.ChiNhanh;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Entity
@Table(name = "tai_khoan_chi_nhanh")
@Data
@IdClass(TaiKhoanChiNhanh.TaiKhoanChiNhanhId.class) // Khóa chính kép
public class TaiKhoanChiNhanh {

    @Id
    @Column(name = "ma_tk")
    private Integer maTk;

    @Id
    @Column(name = "ma_chi_nhanh")
    private Integer maChiNhanh;

    @Data
    public static class TaiKhoanChiNhanhId implements Serializable {
        private Integer maTk;
        private Integer maChiNhanh;
    }
}