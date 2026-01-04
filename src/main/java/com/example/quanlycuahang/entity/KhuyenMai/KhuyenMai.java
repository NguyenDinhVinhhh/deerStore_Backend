package com.example.quanlycuahang.entity.KhuyenMai;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "khuyen_mai")
public class KhuyenMai {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_km")
    private Integer maKm;

    @Column(name = "ten_km", nullable = false, length = 100)
    private String tenKm;

    @Column(name = "mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "ma_code", unique = true, length = 50)
    private String maCode;

    @Column(name = "loai_km", length = 50)
    private String loaiKm;

    @Column(name = "gia_tri", precision = 10, scale = 2)
    private BigDecimal giaTri;

    @Column(name = "dieu_kien_ap_dung", length = 255)
    private String dieuKienApDung;

    @Column(name = "ngay_bat_dau")
    private LocalDate ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    private LocalDate ngayKetThuc;

    @Column(name = "trang_thai", columnDefinition = "TINYINT default 1")
    private Integer trangThai;


    @Column(name = "gia_tri_don_hang_toi_thieu", precision = 18, scale = 0)
    private BigDecimal giaTriDonHangToiThieu;


    @Column(name = "gioi_han_tien_giam_toi_da", precision = 18, scale = 0)
    private BigDecimal gioiHanTienGiamToiDa;


    public BigDecimal getGiaTriDonHangToiThieu() {
        return giaTriDonHangToiThieu;
    }

    public void setGiaTriDonHangToiThieu(BigDecimal giaTriDonHangToiThieu) {
        this.giaTriDonHangToiThieu = giaTriDonHangToiThieu;
    }

    public BigDecimal getGioiHanTienGiamToiDa() {
        return gioiHanTienGiamToiDa;
    }

    public void setGioiHanTienGiamToiDa(BigDecimal gioiHanTienGiamToiDa) {
        this.gioiHanTienGiamToiDa = gioiHanTienGiamToiDa;
    }
    public Integer getMaKm() { return maKm; }
    public void setMaKm(Integer maKm) { this.maKm = maKm; }
    public String getTenKm() { return tenKm; }
    public void setTenKm(String tenKm) { this.tenKm = tenKm; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
    public String getMaCode() { return maCode; }
    public void setMaCode(String maCode) { this.maCode = maCode; }
    public String getLoaiKm() { return loaiKm; }
    public void setLoaiKm(String loaiKm) { this.loaiKm = loaiKm; }
    public BigDecimal getGiaTri() { return giaTri; }
    public void setGiaTri(BigDecimal giaTri) { this.giaTri = giaTri; }
    public String getDieuKienApDung() { return dieuKienApDung; }
    public void setDieuKienApDung(String dieuKienApDung) { this.dieuKienApDung = dieuKienApDung; }
    public LocalDate getNgayBatDau() { return ngayBatDau; }
    public void setNgayBatDau(LocalDate ngayBatDau) { this.ngayBatDau = ngayBatDau; }
    public LocalDate getNgayKetThuc() { return ngayKetThuc; }
    public void setNgayKetThuc(LocalDate ngayKetThuc) { this.ngayKetThuc = ngayKetThuc; }
    public Integer getTrangThai() { return trangThai; }
    public void setTrangThai(Integer trangThai) { this.trangThai = trangThai; }
}