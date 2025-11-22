package com.example.quanlycuahang.dto.HoaDon;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InvoiceResponseHeaderDto {
    private Integer ma_hd;
    private LocalDateTime ngay_lap;
    private String trang_thai;
    private BigDecimal tong_tien;
    private BigDecimal giam_gia;
    private BigDecimal thanh_tien;
    private BigDecimal tien_khach_tra;

    private Integer ma_tk; // Mã nhân viên
    private Integer ma_chi_nhanh;
    private Integer ma_kh; // Mã khách hàng
    private String ghi_chu;
    private String ma_voucher_su_dung;


    public Integer getMa_hd() { return ma_hd; }
    public void setMa_hd(Integer ma_hd) { this.ma_hd = ma_hd; }
    public LocalDateTime getNgay_lap() { return ngay_lap; }
    public void setNgay_lap(LocalDateTime ngay_lap) { this.ngay_lap = ngay_lap; }
    public String getTrang_thai() { return trang_thai; }
    public void setTrang_thai(String trang_thai) { this.trang_thai = trang_thai; }
    public BigDecimal getTong_tien() { return tong_tien; }
    public void setTong_tien(BigDecimal tong_tien) { this.tong_tien = tong_tien; }
    public BigDecimal getGiam_gia() { return giam_gia; }
    public void setGiam_gia(BigDecimal giam_gia) { this.giam_gia = giam_gia; }
    public BigDecimal getThanh_tien() { return thanh_tien; }
    public void setThanh_tien(BigDecimal thanh_tien) { this.thanh_tien = thanh_tien; }
    public BigDecimal getTien_khach_tra() { return tien_khach_tra; }
    public void setTien_khach_tra(BigDecimal tien_khach_tra) { this.tien_khach_tra = tien_khach_tra; }
    public Integer getMa_tk() { return ma_tk; }
    public void setMa_tk(Integer ma_tk) { this.ma_tk = ma_tk; }
    public Integer getMa_chi_nhanh() { return ma_chi_nhanh; }
    public void setMa_chi_nhanh(Integer ma_chi_nhanh) { this.ma_chi_nhanh = ma_chi_nhanh; }
    public Integer getMa_kh() { return ma_kh; }
    public void setMa_kh(Integer ma_kh) { this.ma_kh = ma_kh; }
    public String getGhi_chu() { return ghi_chu; }
    public void setGhi_chu(String ghi_chu) { this.ghi_chu = ghi_chu; }
    public String getMa_voucher_su_dung() { return ma_voucher_su_dung; }
    public void setMa_voucher_su_dung(String ma_voucher_su_dung) { this.ma_voucher_su_dung = ma_voucher_su_dung; }
}