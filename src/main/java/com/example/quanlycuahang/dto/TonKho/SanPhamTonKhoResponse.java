package com.example.quanlycuahang.dto.TonKho;

public class SanPhamTonKhoResponse {
    private Integer maSp;
    private String tenSp;
    private String maSku;
    private Integer soLuongTon;
    private Boolean daThietLapTonKho;

    public SanPhamTonKhoResponse(Integer maSp, String tenSp, String maSku, Integer soLuongTon, Boolean daThietLapTonKho) {
        this.maSp = maSp;
        this.tenSp = tenSp;
        this.maSku = maSku;
        this.soLuongTon = soLuongTon;
        this.daThietLapTonKho = daThietLapTonKho;
    }

    public SanPhamTonKhoResponse() {
    }

    public Integer getMaSp() {
        return maSp;
    }

    public void setMaSp(Integer maSp) {
        this.maSp = maSp;
    }

    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    public String getMaSku() {
        return maSku;
    }

    public void setMaSku(String maSku) {
        this.maSku = maSku;
    }

    public Integer getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(Integer soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public Boolean getDaThietLapTonKho() {
        return daThietLapTonKho;
    }

    public void setDaThietLapTonKho(Boolean daThietLapTonKho) {
        this.daThietLapTonKho = daThietLapTonKho;
    }
}
