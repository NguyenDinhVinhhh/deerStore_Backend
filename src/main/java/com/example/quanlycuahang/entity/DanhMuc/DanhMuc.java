package com.example.quanlycuahang.entity.DanhMuc;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "danh_muc")
@Data
public class DanhMuc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_danh_muc")
    private Integer maDanhMuc;

    @Column(name = "ten_danh_muc", nullable = false)
    private String tenDanhMuc;

    @Column(name = "mo_ta", columnDefinition = "text")
    private String moTa;

    @Column(name = "trang_thai")
    private Boolean trangThai = true;


}