package com.example.quanlycuahang.entity.HoaDon;

import com.example.quanlycuahang.entity.SanPham.SanPham;
import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
@Table(name = "chi_tiet_hoa_don")
public class ChiTietHoaDon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ma_cthd;

    // ====== QUAN HỆ HÓA ĐƠN ======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_hd", insertable = false, updatable = false)
    private HoaDon hoaDon;

    // ====== QUAN HỆ SẢN PHẨM ======
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_sp", insertable = false, updatable = false)
    private SanPham sanPham;

    // ====== GIỮ ID CŨ ======
    @Column(name = "ma_hd")
    private Integer ma_hd;

    @Column(name = "ma_sp")
    private Integer ma_sp;

    @Column(name = "so_luong")
    private Integer so_luong;

    @Column(name = "don_gia", precision = 10, scale = 2)
    private BigDecimal don_gia;

    public Integer getMa_cthd() {
        return ma_cthd;
    }

    public void setMa_cthd(Integer ma_cthd) {
        this.ma_cthd = ma_cthd;
    }

    public HoaDon getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    public SanPham getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public Integer getMa_hd() {
        return ma_hd;
    }

    public void setMa_hd(Integer ma_hd) {
        this.ma_hd = ma_hd;
    }

    public Integer getMa_sp() {
        return ma_sp;
    }

    public void setMa_sp(Integer ma_sp) {
        this.ma_sp = ma_sp;
    }

    public Integer getSo_luong() {
        return so_luong;
    }

    public void setSo_luong(Integer so_luong) {
        this.so_luong = so_luong;
    }

    public BigDecimal getDon_gia() {
        return don_gia;
    }

    public void setDon_gia(BigDecimal don_gia) {
        this.don_gia = don_gia;
    }
}
