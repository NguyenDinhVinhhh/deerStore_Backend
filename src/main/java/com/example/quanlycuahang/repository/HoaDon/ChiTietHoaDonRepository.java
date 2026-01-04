package com.example.quanlycuahang.repository.HoaDon;

import com.example.quanlycuahang.dto.HoaDon.TopSanPham;
import com.example.quanlycuahang.dto.HoaDon.TopSanPhamDTO;
import com.example.quanlycuahang.entity.HoaDon.ChiTietHoaDon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List; // 💡 Cần import List

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, Integer> {

    /**
     * Tìm tất cả ChiTietHoaDon (danh sách sản phẩm) dựa trên Mã Hóa Đơn (maHd).
     * Hàm này được sử dụng trong finalizeOnlinePayment để lấy thông tin trừ tồn kho.
     *
     * @param maHd Mã hóa đơn cần tìm.
     * @return Danh sách ChiTietHoaDon.
     */
    @Query("SELECT cthd FROM ChiTietHoaDon cthd WHERE cthd.ma_hd = :maHd")
    List<ChiTietHoaDon> findByMaHdQuery(@Param("maHd") int maHd); // ✅ Sử dụng @Query

    @Query("""
    SELECT new com.example.quanlycuahang.dto.HoaDon.TopSanPhamDTO(
        sp.maSp,
        sp.tenSp,
        sp.maSku,
        COUNT(DISTINCT hd.ma_hd),
        SUM(ct.so_luong),
        SUM(ct.so_luong * ct.don_gia)
    )
    FROM ChiTietHoaDon ct
    JOIN ct.hoaDon hd
    JOIN ct.sanPham sp
    WHERE hd.trang_thai = 'HOAN THANH'
      AND hd.ngay_lap BETWEEN :start AND :end
      AND (:maChiNhanh IS NULL OR hd.ma_chi_nhanh = :maChiNhanh)
    GROUP BY sp.maSp, sp.tenSp, sp.maSku
    ORDER BY COUNT(DISTINCT hd.ma_hd) DESC
""")
    List<TopSanPhamDTO> topSanPhamTheoDonHang(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("maChiNhanh") Integer maChiNhanh,
            Pageable pageable
    );


    @Query("""
    SELECT new com.example.quanlycuahang.dto.HoaDon.TopSanPhamDTO(
        sp.maSp,
        sp.tenSp,
        sp.maSku,
        COUNT(DISTINCT hd.ma_hd),
        SUM(ct.so_luong),
        SUM(ct.so_luong * ct.don_gia)
    )
    FROM ChiTietHoaDon ct
    JOIN ct.hoaDon hd
    JOIN ct.sanPham sp
    WHERE hd.trang_thai = 'HOAN THANH'
      AND hd.ngay_lap BETWEEN :start AND :end
      AND (:maChiNhanh IS NULL OR hd.ma_chi_nhanh = :maChiNhanh)
    GROUP BY sp.maSp, sp.tenSp, sp.maSku
    ORDER BY SUM(ct.so_luong) DESC
            
""")
    List<TopSanPhamDTO> topSanPhamTheoSoLuong(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("maChiNhanh") Integer maChiNhanh,
            Pageable pageable
    );

    @Query("""
    SELECT new com.example.quanlycuahang.dto.HoaDon.TopSanPhamDTO(
        sp.maSp,
        sp.tenSp,
        sp.maSku,
        COUNT(DISTINCT hd.ma_hd),
       SUM(ct.so_luong),
        SUM(ct.so_luong * ct.don_gia)
    )
    FROM ChiTietHoaDon ct
    JOIN ct.hoaDon hd
    JOIN ct.sanPham sp
    WHERE hd.trang_thai = 'HOAN THANH'
      AND hd.ngay_lap BETWEEN :start AND :end
      AND (:maChiNhanh IS NULL OR hd.ma_chi_nhanh = :maChiNhanh)
    GROUP BY sp.maSp, sp.tenSp, sp.maSku
   ORDER BY SUM(ct.so_luong * ct.don_gia) DESC
            
""")
    List<TopSanPhamDTO> topSanPhamTheoDoanhThu(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("maChiNhanh") Integer maChiNhanh,
            Pageable pageable
    );

    @Query("""
    SELECT c FROM ChiTietHoaDon c
    WHERE c.ma_hd = :maHd
""")
    List<ChiTietHoaDon> findByHoaDon(@Param("maHd") Integer maHd);




}