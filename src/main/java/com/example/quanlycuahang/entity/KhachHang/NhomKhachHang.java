package com.example.quanlycuahang.entity.KhachHang;


import jakarta.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "nhom_khach_hang")
public class NhomKhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_nhom")
    private Integer maNhom;

    @Column(name = "ten_nhom", nullable = false, length = 100)
    private String tenNhom;

    @Column(name = "mo_ta", length = 255)
    private String moTa;

    @Column(name = "trang_thai")
    private Boolean trangThai;


    @Column(name = "phan_tram_chiet_khau", nullable = false, precision = 5, scale = 2)
    private BigDecimal phanTramChietKhau;

    @Column(name = "nguong_chi_tieu_toi_thieu", nullable = false, precision = 18, scale = 0)
    private BigDecimal nguongChiTieuToiThieu;

    @Column(name = "gioi_han_tien_giam_toi_da", nullable = false, precision = 18, scale = 0)
    private BigDecimal gioiHanTienGiamToiDa;




    public BigDecimal getGioiHanTienGiamToiDa() {
        return gioiHanTienGiamToiDa;
    }

    public void setGioiHanTienGiamToiDa(BigDecimal gioiHanTienGiamToiDa) {
        this.gioiHanTienGiamToiDa = gioiHanTienGiamToiDa;
    }

    public Integer getMaNhom() { return maNhom; }
    public void setMaNhom(Integer maNhom) { this.maNhom = maNhom; }
    public String getTenNhom() { return tenNhom; }
    public void setTenNhom(String tenNhom) { this.tenNhom = tenNhom; }
    public BigDecimal getPhanTramChietKhau() { return phanTramChietKhau; }
    public void setPhanTramChietKhau(BigDecimal phanTramChietKhau) { this.phanTramChietKhau = phanTramChietKhau; }
    public BigDecimal getNguongChiTieuToiThieu() { return nguongChiTieuToiThieu; }
    public void setNguongChiTieuToiThieu(BigDecimal nguongChiTieuToiThieu) { this.nguongChiTieuToiThieu = nguongChiTieuToiThieu; }
    public Boolean getTrangThai() { return trangThai; }
    public void setTrangThai(Boolean trangThai) { this.trangThai = trangThai; }
    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }
}