package com.example.quanlycuahang.entity.TonKho;

import com.example.quanlycuahang.entity.KhoHang.KhoHang;
import com.example.quanlycuahang.entity.SanPham.SanPham;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "ton_kho")
@Data
public class TonKho {

    // Khóa chính phức hợp
    @EmbeddedId
    private TonKhoId id;

    // Quan hệ: Mối quan hệ Many-to-One tới SanPham
    @ManyToOne
    @MapsId("maSp") // Map trường 'maSp' trong TonKhoId
    @JoinColumn(name = "ma_sp", referencedColumnName = "ma_sp")
    private SanPham sanPham;

    // Quan hệ: Mối quan hệ Many-to-One tới KhoHang
    @ManyToOne
    @MapsId("maKho") // Map trường 'maKho' trong TonKhoId
    @JoinColumn(name = "ma_kho", referencedColumnName = "ma_kho")
    private KhoHang khoHang;

    @Column(name = "so_luong_ton", columnDefinition = "INT DEFAULT 0")
    private Integer soLuongTon = 0;

    @Column(name = "ngay_cap_nhat")
    private LocalDateTime ngayCapNhat;

    @Column(name = "ghi_chu", columnDefinition = "TEXT")
    private String ghiChu;
}