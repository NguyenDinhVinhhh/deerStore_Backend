package com.example.quanlycuahang.entity.KhoHang;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "kho_hang")
@Data
public class KhoHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_kho")
    private Integer maKho;

    @Column(name = "ten_kho", length = 100)
    private String tenKho;

    @Column(name = "dia_chi_kho")
    private String diaChiKho;

    @Column(name = "ma_chi_nhanh")
    private Integer maChiNhanh;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

}
