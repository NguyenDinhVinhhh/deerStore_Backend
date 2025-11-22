package com.example.quanlycuahang.dto.KhachHang;

import com.example.quanlycuahang.entity.KhachHang.KhachHang;
import com.example.quanlycuahang.entity.KhachHang.NhomKhachHang;

import java.math.BigDecimal;
import java.time.LocalDate;

public class KhachHangResponse {


    private Integer maKh;
    private String hoTen;
    private String sdt;
    private String email;
    private String diaChi;
    private LocalDate ngayDangKy;
    private String ghiChu;
    private BigDecimal tongChiTieuLuyKe;
    private Long tongSoDonHang;

    private Integer maNhom;
    private String tenNhom;
    private BigDecimal phanTramChietKhau;
    private BigDecimal nguongChiTieuToiThieu;


    public KhachHangResponse() {
    }

    public KhachHangResponse(KhachHang khachHang) {
        this.maKh = khachHang.getMaKh();
        this.hoTen = khachHang.getHoTen();
        this.sdt = khachHang.getSdt();
        this.email = khachHang.getEmail();
        this.diaChi = khachHang.getDiaChi();
        this.ngayDangKy = khachHang.getNgayDangKy();
        this.ghiChu = khachHang.getGhiChu();
        this.tongChiTieuLuyKe = khachHang.getTongChiTieuLuyKe();
        // tongSoDonHang phải được set thủ công sau khi gọi service count.

        NhomKhachHang nhom = khachHang.getNhomKhachHang();
        if (nhom != null) {
            this.maNhom = nhom.getMaNhom();
            this.tenNhom = nhom.getTenNhom();
            this.phanTramChietKhau = nhom.getPhanTramChietKhau();
            this.nguongChiTieuToiThieu = nhom.getNguongChiTieuToiThieu();
        } else {
            this.tenNhom = "Không xác định";
            this.phanTramChietKhau = BigDecimal.ZERO;
            this.nguongChiTieuToiThieu = BigDecimal.ZERO;
        }
    }


    public Long getTongSoDonHang() {
        return tongSoDonHang;
    }
    public void setTongSoDonHang(Long tongSoDonHang) {
        this.tongSoDonHang = tongSoDonHang;
    }
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
    public Integer getMaNhom() { return maNhom; }
    public void setMaNhom(Integer maNhom) { this.maNhom = maNhom; }
    public String getTenNhom() { return tenNhom; }
    public void setTenNhom(String tenNhom) { this.tenNhom = tenNhom; }
    public BigDecimal getPhanTramChietKhau() { return phanTramChietKhau; }
    public void setPhanTramChietKhau(BigDecimal phanTramChietKhau) { this.phanTramChietKhau = phanTramChietKhau; }
    public BigDecimal getNguongChiTieuToiThieu() { return nguongChiTieuToiThieu; }
    public void setNguongChiTieuToiThieu(BigDecimal nguongChiTieuToiThieu) { this.nguongChiTieuToiThieu = nguongChiTieuToiThieu; }
}