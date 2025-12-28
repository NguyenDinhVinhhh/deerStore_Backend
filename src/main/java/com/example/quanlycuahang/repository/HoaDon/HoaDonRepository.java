package com.example.quanlycuahang.repository.HoaDon;

import com.example.quanlycuahang.dto.HoaDon.HoaDonResponse;
import com.example.quanlycuahang.dto.ThongKe.DoanhThuChiNhanhDTO;
import com.example.quanlycuahang.entity.HoaDon.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

    // ================== THỐNG KÊ THEO KHÁCH HÀNG ==================

    /**
     * Đếm tổng số hóa đơn của 1 khách hàng
     */
    @Query("SELECT COUNT(h) FROM HoaDon h WHERE h.ma_kh = :maKh")
    Long countAllByMaKh(@Param("maKh") Integer maKh);

    @Query("""
    SELECT h FROM HoaDon h
    WHERE h.ma_kh = :maKh
    ORDER BY h.ngay_lap DESC
""")
    List<HoaDon> findLichSuMuaHang(@Param("maKh") Integer maKh);



    // ================== DASHBOARD HÔM NAY ==================

    // 1️⃣ Đơn mới hôm nay (theo chi nhánh hoặc tất cả)
    @Query("""
        SELECT COUNT(hd)
        FROM HoaDon hd
        WHERE hd.ngay_lap BETWEEN :start AND :end
          AND (:maChiNhanh IS NULL OR hd.ma_chi_nhanh = :maChiNhanh)
    """)
    Long countDonHangMoiHomNay(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("maChiNhanh") Integer maChiNhanh
    );

    // 2️⃣ Doanh thu hôm nay
    @Query("""
        SELECT COALESCE(SUM(hd.thanh_tien), 0)
        FROM HoaDon hd
        WHERE hd.trang_thai = 'HOAN THANH'
          AND hd.ngay_lap BETWEEN :start AND :end
          AND (:maChiNhanh IS NULL OR hd.ma_chi_nhanh = :maChiNhanh)
    """)
    BigDecimal tongDoanhThuHomNay(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("maChiNhanh") Integer maChiNhanh
    );

    // 3️⃣ Đơn trả hàng
    @Query("""
        SELECT COUNT(hd)
        FROM HoaDon hd
        WHERE hd.trang_thai = 'DON TRA'
          AND hd.ngay_lap BETWEEN :start AND :end
          AND (:maChiNhanh IS NULL OR hd.ma_chi_nhanh = :maChiNhanh)
    """)
    Long countDonTraHang(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("maChiNhanh") Integer maChiNhanh
    );


    // 3️⃣ Đơn hủy
    @Query("""
        SELECT COUNT(hd)
        FROM HoaDon hd
        WHERE hd.trang_thai = 'DON HUY'
          AND hd.ngay_lap BETWEEN :start AND :end
          AND (:maChiNhanh IS NULL OR hd.ma_chi_nhanh = :maChiNhanh)
    """)
    Long countDonHuy(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("maChiNhanh") Integer maChiNhanh
    );

    //doanh thu bieu do
    @Query("""
    SELECT 
        DATE(hd.ngay_lap),
        COALESCE(SUM(hd.thanh_tien), 0)
    FROM HoaDon hd
    WHERE hd.trang_thai = 'HOAN THANH'
      AND hd.ngay_lap BETWEEN :start AND :end
      AND (:maChiNhanh IS NULL OR hd.ma_chi_nhanh = :maChiNhanh)
    GROUP BY DATE(hd.ngay_lap)
    ORDER BY DATE(hd.ngay_lap)
""")
    List<Object[]> doanhThuTheoNgay(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("maChiNhanh") Integer maChiNhanh
    );

    @Query("""
    SELECT 
        MONTH(hd.ngay_lap),
        COALESCE(SUM(hd.thanh_tien), 0)
    FROM HoaDon hd
    WHERE hd.trang_thai = 'HOAN THANH'
      AND YEAR(hd.ngay_lap) = :year
      AND (:maChiNhanh IS NULL OR hd.ma_chi_nhanh = :maChiNhanh)
    GROUP BY MONTH(hd.ngay_lap)
    ORDER BY MONTH(hd.ngay_lap)
""")
    List<Object[]> doanhThuTheoThang(
            @Param("year") int year,
            @Param("maChiNhanh") Integer maChiNhanh
    );


    @Query("""
    SELECT new com.example.quanlycuahang.dto.ThongKe.DoanhThuChiNhanhDTO(
        hd.ma_chi_nhanh,
        cn.tenChiNhanh,
        COALESCE(SUM(hd.thanh_tien), 0)
    )
    FROM HoaDon hd
    JOIN ChiNhanh cn ON cn.maChiNhanh = hd.ma_chi_nhanh
    WHERE hd.trang_thai = 'HOAN THANH'
      AND hd.ngay_lap BETWEEN :start AND :end
    GROUP BY hd.ma_chi_nhanh, cn.tenChiNhanh
""")
    List<DoanhThuChiNhanhDTO> thongKeDoanhThuTheoChiNhanh(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );


    //lay dsanh sách hóa đơn
    @Query("""
    SELECT new com.example.quanlycuahang.dto.HoaDon.HoaDonResponse(
        hd.ma_hd,
        hd.ngay_lap,
        hd.thanh_tien,
        hd.trang_thai,
        hd.ma_chi_nhanh
    )
    FROM HoaDon hd
    WHERE (:maChiNhanh IS NULL OR hd.ma_chi_nhanh = :maChiNhanh)
      AND (:trangThai IS NULL OR hd.trang_thai = :trangThai)
      AND (:start IS NULL OR hd.ngay_lap >= :start)
      AND (:end IS NULL OR hd.ngay_lap <= :end)
    ORDER BY hd.ngay_lap DESC
""")
    Page<HoaDonResponse> getDanhSachHoaDon(
            @Param("maChiNhanh") Integer maChiNhanh,
            @Param("trangThai") String trangThai,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );

}
