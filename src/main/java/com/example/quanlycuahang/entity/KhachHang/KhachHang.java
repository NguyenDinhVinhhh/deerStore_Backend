package com.example.quanlycuahang.entity.KhachHang;

import com.example.quanlycuahang.entity.TaiKhoanKhachHang.TaiKhoanKhachHang;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;


@Entity
@Table(name = "khach_hang")
public class KhachHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_kh")
    private Integer maKh;

    @Column(name = "ho_ten", nullable = false, length = 255)
    private String hoTen;

    @Column(name = "sdt", length = 50, unique = true)
    private String sdt;

    @Column(name = "email", length = 255)
    private String email;

    @Column(name = "dia_chi", length = 255)
    private String diaChi;

    @Column(name = "ngay_dang_ky")
    private LocalDate ngayDangKy;

    @Column(name = "ghi_chu", length = 500)
    private String ghiChu;


    @Column(name = "tong_chi_tieu_luy_ke", nullable = false, precision = 18, scale = 0)
    private BigDecimal tongChiTieuLuyKe = BigDecimal.ZERO;

    // Liên kết Khóa ngoại với NhomKhachHang
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_nhom", referencedColumnName = "ma_nhom")
    @JsonIgnore
    private NhomKhachHang nhomKhachHang;


    @OneToOne(mappedBy = "khachHang", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private TaiKhoanKhachHang taiKhoan;

    public TaiKhoanKhachHang getTaiKhoan() {
        return taiKhoan;
    }

    public void setTaiKhoan(TaiKhoanKhachHang taiKhoan) {
        this.taiKhoan = taiKhoan;
    }
    // Getters và Setters
    public Integer getMaKh() { return maKh; }
    public void setMaKh(Integer maKh) { this.maKh = maKh; }
    public String getHoTen() { return hoTen; }
    public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    public LocalDate getNgayDangKy() { return ngayDangKy; }
    public void setNgayDangKy(LocalDate ngayDangKy) { this.ngayDangKy = ngayDangKy; }
    public String getGhiChu() { return ghiChu; }
    public void setGhiChu(String ghiChu) { this.ghiChu = ghiChu; }
    public BigDecimal getTongChiTieuLuyKe() { return tongChiTieuLuyKe; }
    public void setTongChiTieuLuyKe(BigDecimal tongChiTieuLuyKe) { this.tongChiTieuLuyKe = tongChiTieuLuyKe; }
    public NhomKhachHang getNhomKhachHang() { return nhomKhachHang; }
    public void setNhomKhachHang(NhomKhachHang nhomKhachHang) { this.nhomKhachHang = nhomKhachHang; }
}