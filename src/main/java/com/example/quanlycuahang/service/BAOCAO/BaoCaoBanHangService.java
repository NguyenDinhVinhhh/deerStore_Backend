package com.example.quanlycuahang.service.BAOCAO;

import com.example.quanlycuahang.dto.HoaDon.TopSanPhamDTO;
import com.example.quanlycuahang.repository.HoaDon.ChiTietHoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
@Service
public class BaoCaoBanHangService {
    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;
    /**
     * Lấy TOP sản phẩm bán chạy
     *
     * @param range TODAY | YESTERDAY | 7_DAYS | MONTH | YEAR
     * @param type ORDER | QUANTITY | REVENUE
     * @param maChiNhanh nullable
     * @param limit số lượng top
     */
    public List<TopSanPhamDTO> getTopSanPham(
            String range,
            String type,
            Integer maChiNhanh,
            int limit
    ) {

        // 1️⃣ Xác định thời gian
        LocalDateTime start;
        LocalDateTime end = LocalDateTime.now();

        switch (range.toUpperCase()) {
            case "TODAY" -> {
                start = LocalDate.now().atStartOfDay();
                end = LocalDate.now().atTime(LocalTime.MAX);
            }
            case "YESTERDAY" -> {
                start = LocalDate.now().minusDays(1).atStartOfDay();
                end = LocalDate.now().minusDays(1).atTime(LocalTime.MAX);
            }
            case "7_DAYS" -> start = LocalDateTime.now().minusDays(7);
            case "MONTH" -> start = LocalDate.now().withDayOfMonth(1).atStartOfDay();
            case "YEAR" -> start = LocalDate.now().withDayOfYear(1).atStartOfDay();
            default -> throw new IllegalArgumentException("Khoảng thời gian không hợp lệ");
        }

        PageRequest pageable = PageRequest.of(0, limit);

        // 2️⃣ Chọn query theo loại thống kê
        return switch (type.toUpperCase()) {

            case "ORDER" -> chiTietHoaDonRepository
                    .topSanPhamTheoDonHang(start, end, maChiNhanh, pageable);

            case "QUANTITY" -> chiTietHoaDonRepository
                    .topSanPhamTheoSoLuong(start, end, maChiNhanh, pageable);

            case "REVENUE" -> chiTietHoaDonRepository
                    .topSanPhamTheoDoanhThu(start, end, maChiNhanh, pageable);

            default -> throw new IllegalArgumentException("Loại thống kê không hợp lệ");
        };
    }
}
