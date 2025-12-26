package com.example.quanlycuahang.dto.ThongKe;



import java.math.BigDecimal;

public class ThongKe_Donhang_Doanhthu_Request {
    private Long tongDonHangMoi;
    private BigDecimal tongDoanhThu;
    private Long tongDonTraHang;
    private Long tongDonHuy;

    public Long getTongDonHuy() {
        return tongDonHuy;
    }

    public void setTongDonHuy(Long tongDonHuy) {
        this.tongDonHuy = tongDonHuy;
    }

    private Integer maChiNhanh;

    public ThongKe_Donhang_Doanhthu_Request(Long tongDonHangMoi, BigDecimal tongDoanhThu, Long tongDonTraHang, Long tongDonHuy, Integer maChiNhanh) {
        this.tongDonHangMoi = tongDonHangMoi;
        this.tongDoanhThu = tongDoanhThu;
        this.tongDonTraHang = tongDonTraHang;
        this.tongDonHuy = tongDonHuy;
        this.maChiNhanh = maChiNhanh;
    }

    public ThongKe_Donhang_Doanhthu_Request() {
    }

    public Integer getMaChiNhanh() {
        return maChiNhanh;
    }

    public void setMaChiNhanh(Integer maChiNhanh) {
        this.maChiNhanh = maChiNhanh;
    }

    public Long getTongDonHangMoi() {
        return tongDonHangMoi;
    }

    public void setTongDonHangMoi(Long tongDonHangMoi) {
        this.tongDonHangMoi = tongDonHangMoi;
    }

    public BigDecimal getTongDoanhThu() {
        return tongDoanhThu;
    }

    public void setTongDoanhThu(BigDecimal tongDoanhThu) {
        this.tongDoanhThu = tongDoanhThu;
    }

    public Long getTongDonTraHang() {
        return tongDonTraHang;
    }

    public void setTongDonTraHang(Long tongDonTraHang) {
        this.tongDonTraHang = tongDonTraHang;
    }


}
