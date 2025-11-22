package com.example.quanlycuahang.entity.SanPham;

import com.example.quanlycuahang.entity.DanhMuc.DanhMuc;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "san_pham")

public class SanPham {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_sp")
    private Integer maSp;

    @Column(name = "ma_sku", unique = true)
    private String maSku;

    @Column(name = "ten_sp", nullable = false)
    private String tenSp;

    @Column(name = "mo_ta", columnDefinition = "text")
    private String moTa;

    @Column(name = "don_gia", precision = 10, scale = 2)
    private BigDecimal donGia;

    @Column(name = "gia_von", precision = 10, scale = 2)
    private BigDecimal giaVon;

    @Column(name = "hinh_anh")
    private String hinhAnh;

    @Column(name = "ma_danh_muc")
    private Integer maDanhMuc;

    @Column(name = "trang_thai")
    private Boolean trangThai = true;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_danh_muc", referencedColumnName = "ma_danh_muc", insertable = false, updatable = false)
    private DanhMuc danhMuc;



    public SanPham() {
    }



    public Integer getMaSp() {
        return maSp;
    }

    public void setMaSp(Integer maSp) {
        this.maSp = maSp;
    }


    public String getMaSku() {
        return maSku;
    }

    public void setMaSku(String maSku) {
        this.maSku = maSku;
    }


    public String getTenSp() {
        return tenSp;
    }

    public void setTenSp(String tenSp) {
        this.tenSp = tenSp;
    }

    // Getter và Setter cho moTa
    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }


    public BigDecimal getDonGia() {
        return donGia;
    }

    public void setDonGia(BigDecimal donGia) {
        this.donGia = donGia;
    }


    public BigDecimal getGiaVon() {
        return giaVon;
    }

    public void setGiaVon(BigDecimal giaVon) {
        this.giaVon = giaVon;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public Integer getMaDanhMuc() {
        return maDanhMuc;
    }

    public void setMaDanhMuc(Integer maDanhMuc) {
        this.maDanhMuc = maDanhMuc;
    }


    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
    }


    public DanhMuc getDanhMuc() {
        return danhMuc;
    }

    public void setDanhMuc(DanhMuc danhMuc) {
        this.danhMuc = danhMuc;
    }
}