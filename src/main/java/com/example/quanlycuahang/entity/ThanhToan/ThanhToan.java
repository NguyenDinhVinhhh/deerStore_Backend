package com.example.quanlycuahang.entity.ThanhToan;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "thanh_toan")
public class ThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ma_tt;

    private Integer ma_hd;

    @Column(name = "phuong_thuc", length = 50)
    private String phuong_thuc;

    @Column(name = "so_tien", precision = 12, scale = 2)
    private BigDecimal so_tien;

    private LocalDateTime ngay_thanh_toan = LocalDateTime.now();

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghi_chu;

    // Getters and Setters
    public Integer getMa_tt() { return ma_tt; }
    public void setMa_tt(Integer ma_tt) { this.ma_tt = ma_tt; }
    public Integer getMa_hd() { return ma_hd; }
    public void setMa_hd(Integer ma_hd) { this.ma_hd = ma_hd; }
    public String getPhuong_thuc() { return phuong_thuc; }
    public void setPhuong_thuc(String phuong_thuc) { this.phuong_thuc = phuong_thuc; }
    public BigDecimal getSo_tien() { return so_tien; }
    public void setSo_tien(BigDecimal so_tien) { this.so_tien = so_tien; }
    public LocalDateTime getNgay_thanh_toan() { return ngay_thanh_toan; }
    public void setNgay_thanh_toan(LocalDateTime ngay_thanh_toan) { this.ngay_thanh_toan = ngay_thanh_toan; }
    public String getGhi_chu() { return ghi_chu; }
    public void setGhi_chu(String ghi_chu) { this.ghi_chu = ghi_chu; }
}