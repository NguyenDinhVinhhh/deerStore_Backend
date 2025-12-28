package com.example.quanlycuahang.service.LichSuMuaHang;

import com.example.quanlycuahang.dto.LichSuMuaHang.ChiTietHoaDonResponse;
import com.example.quanlycuahang.dto.LichSuMuaHang.LichSuMuaHangResponse;
import com.example.quanlycuahang.entity.HoaDon.ChiTietHoaDon;
import com.example.quanlycuahang.entity.HoaDon.HoaDon;
import com.example.quanlycuahang.entity.KhachHang.KhachHang;
import com.example.quanlycuahang.repository.HoaDon.HoaDonRepository;
import com.example.quanlycuahang.repository.KhachHang.KhachHangRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LichSuMuaHangService {

    private final KhachHangRepository khachHangRepository;
    private final HoaDonRepository hoaDonRepository;

    public List<LichSuMuaHangResponse> getLichSuMuaHang(Integer makh) {
        // 1. Tìm khách hàng
        KhachHang kh = khachHangRepository.findByMaKh(makh)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng"));

        // 2. Lấy danh sách hóa đơn từ Repository
        List<HoaDon> hoaDons = hoaDonRepository.findLichSuMuaHang(kh.getMaKh());

        // 3. Mapping Entity -> DTO
        return hoaDons.stream().map(hd -> {
            LichSuMuaHangResponse response = new LichSuMuaHangResponse();
            response.setMaHoaDon(hd.getMa_hd());
            response.setNgayMua(hd.getNgay_lap());

            // ✅ Lấy trực tiếp các cột tiền từ bảng hoa_don
            response.setTongTienGoc(hd.getTong_tien());
            response.setGiamGia(hd.getGiam_gia() != null ? hd.getGiam_gia() : BigDecimal.ZERO);
            response.setThanhTien(hd.getThanh_tien()); // Đây là số tiền thực thu sau khi giảm giá

            // Map danh sách chi tiết sản phẩm
            List<ChiTietHoaDonResponse> chiTietResponses = hd.getChiTietHoaDon().stream()
                    .map(this::mapChiTiet)
                    .collect(Collectors.toList());
            response.setChiTiet(chiTietResponses);

            return response;
        }).collect(Collectors.toList());
    }

    private ChiTietHoaDonResponse mapChiTiet(ChiTietHoaDon ct) {
        // Lấy đơn giá và số lượng từ bảng chi_tiet_hoa_don
        BigDecimal donGia = ct.getDon_gia() != null ? ct.getDon_gia() : BigDecimal.ZERO;
        Integer soLuong = ct.getSo_luong() != null ? ct.getSo_luong() : 0;

        // Thành tiền từng món = Đơn giá * Số lượng (nên lấy từ cột thành tiền nếu bảng chi tiết có lưu)
        BigDecimal thanhTienTungMon = donGia.multiply(BigDecimal.valueOf(soLuong));

        return new ChiTietHoaDonResponse(
                ct.getSanPham().getTenSp(),
                soLuong,
                donGia,
                thanhTienTungMon
        );
    }
}