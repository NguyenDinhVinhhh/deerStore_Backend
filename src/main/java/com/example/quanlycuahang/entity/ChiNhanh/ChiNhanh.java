package com.example.quanlycuahang.entity.ChiNhanh;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chi_nhanh")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChiNhanh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_chi_nhanh")
    private Integer maChiNhanh;

    @Column(name = "ten_chi_nhanh", nullable = false)
    private String tenChiNhanh;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "nguoi_quan_ly")
    private String nguoiQuanLy;

    @Column(name = "trang_thai")
    @Builder.Default
    private Boolean trangThai = true;
}
