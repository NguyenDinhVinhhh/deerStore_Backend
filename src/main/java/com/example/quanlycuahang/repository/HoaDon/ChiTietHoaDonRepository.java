package com.example.quanlycuahang.repository.HoaDon;

import com.example.quanlycuahang.entity.HoaDon.ChiTietHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

}