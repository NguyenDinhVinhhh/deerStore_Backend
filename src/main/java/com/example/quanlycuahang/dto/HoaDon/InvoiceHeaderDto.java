package com.example.quanlycuahang.dto.HoaDon;


public class InvoiceHeaderDto {
    private Integer ma_tk;
    private Integer ma_chi_nhanh;
    private Integer ma_kh;
    private Integer ma_km;
    private String ma_voucher_su_dung;
    private String ghi_chu;


    public Integer getMa_tk() { return ma_tk; }
    public void setMa_tk(Integer ma_tk) { this.ma_tk = ma_tk; }
    public Integer getMa_chi_nhanh() { return ma_chi_nhanh; }
    public void setMa_chi_nhanh(Integer ma_chi_nhanh) { this.ma_chi_nhanh = ma_chi_nhanh; }
    public Integer getMa_kh() { return ma_kh; }
    public void setMa_kh(Integer ma_kh) { this.ma_kh = ma_kh; }
    public Integer getMa_km() { return ma_km; }
    public void setMa_km(Integer ma_km) { this.ma_km = ma_km; }
    public String getMa_voucher_su_dung() { return ma_voucher_su_dung; }
    public void setMa_voucher_su_dung(String ma_voucher_su_dung) { this.ma_voucher_su_dung = ma_voucher_su_dung; }
    public String getGhi_chu() { return ghi_chu; }
    public void setGhi_chu(String ghi_chu) { this.ghi_chu = ghi_chu; }
}