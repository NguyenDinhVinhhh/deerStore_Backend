package com.example.quanlycuahang.repository.HoaDon;

import com.example.quanlycuahang.entity.HoaDon.HoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {
    // hàm đếm hóa đơn khách hàng mua mục đích thống kê
    @Query("SELECT COUNT(h) FROM HoaDon h WHERE h.ma_kh = :maKh")
    Long countAllByMaKh(@Param("maKh") Integer maKh);

   // hàm đém tổng hóa đơn có trạng thái hoàn thành của khách hàng đó
    // Long countByMaKhAndTrangThai(@Param("maKh") Integer maKh, @Param("trangThaiHoanThanh") String trangThaiHoanThanh);
}