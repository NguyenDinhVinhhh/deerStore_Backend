package com.example.quanlycuahang.dto.HoaDon;

import java.math.BigDecimal;

public class PaymentDto {
    private String phuong_thuc;
    private BigDecimal so_tien;
    private String ghi_chu; // Optional

    // Getters and Setters
    public String getPhuong_thuc() { return phuong_thuc; }
    public void setPhuong_thuc(String phuong_thuc) { this.phuong_thuc = phuong_thuc; }
    public BigDecimal getSo_tien() { return so_tien; }
    public void setSo_tien(BigDecimal so_tien) { this.so_tien = so_tien; }
    public String getGhi_chu() { return ghi_chu; }
    public void setGhi_chu(String ghi_chu) { this.ghi_chu = ghi_chu; }
}