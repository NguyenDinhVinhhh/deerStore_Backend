package com.example.quanlycuahang.service.HoaDon;

import com.example.quanlycuahang.dto.HoaDon.InvoiceItemDto;
import com.example.quanlycuahang.dto.HoaDon.InvoiceRequest;
import com.example.quanlycuahang.dto.TonKho.InvoiceCalculationResult;
import com.example.quanlycuahang.entity.KhuyenMai.KhuyenMai;
import com.example.quanlycuahang.entity.SanPham.SanPham;
import com.example.quanlycuahang.repository.SanPham.SanPhamRepository;
import com.example.quanlycuahang.service.KhachHang.KhachHangService;
import com.example.quanlycuahang.service.KhuyenMai.KhuyenMaiService; // 💡 BỔ SUNG: Import KhuyenMaiService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

// tính toán giá giảm giá thành tiền
@Service
public class InvoiceCalculationService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private KhachHangService khachHangService;

    // 💡 BỔ SUNG: Inject KhuyenMaiService
    @Autowired
    private KhuyenMaiService khuyenMaiService;

    /**
     * 🎯 Tính tổng giảm giá theo quy tắc: Áp dụng chiết khấu thành viên trước (có giới hạn Max Cap), sau đó áp dụng voucher/khuyến mãi.
     *
     * @param tongTienGoc Tổng tiền gốc của đơn hàng.
     * @param maKh Mã khách hàng (dùng để tra cứu hạng thành viên).
     * @param maVoucher Mã voucher/khuyến mãi.
     * @return Tổng giảm giá (BigDecimal).
     */
    private BigDecimal calculateDiscount(BigDecimal tongTienGoc, Integer maKh, String maVoucher) {

        BigDecimal giamGiaTong = BigDecimal.ZERO;
        BigDecimal tienSauChietKhau = tongTienGoc;

        // 1. TÍNH CHIẾT KHẤU THEO HẠNG THÀNH VIÊN (Kèm theo giới hạn Max Cap)
        if (maKh != null) {
            BigDecimal phanTramChietKhau = khachHangService.getCustomerDiscountPercent(maKh);

            if (phanTramChietKhau.compareTo(BigDecimal.ZERO) > 0) {

                BigDecimal maxCapThanhVien = khachHangService.getMemberDiscountMaxCap(maKh);

                BigDecimal phanTram = phanTramChietKhau.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
                BigDecimal giamGiaThanhVienTinhToan = tongTienGoc.multiply(phanTram);
                giamGiaThanhVienTinhToan = giamGiaThanhVienTinhToan.setScale(0, RoundingMode.HALF_UP);

                BigDecimal giamGiaThanhVien;

                if (maxCapThanhVien.compareTo(BigDecimal.ZERO) > 0 &&
                        giamGiaThanhVienTinhToan.compareTo(maxCapThanhVien) > 0)
                {
                    giamGiaThanhVien = maxCapThanhVien;
                } else {
                    giamGiaThanhVien = giamGiaThanhVienTinhToan;
                }

                giamGiaTong = giamGiaTong.add(giamGiaThanhVien);

                tienSauChietKhau = tongTienGoc.subtract(giamGiaThanhVien);
            }
        }

        // 2. TÍNH GIẢM GIÁ THEO MÃ VOUCHER/KHUYẾN MÃI (Áp dụng trên tienSauChietKhau)
        if (maVoucher != null && !maVoucher.trim().isEmpty()) {

            Optional<KhuyenMai> khuyenMaiOpt = khuyenMaiService.getValidKhuyenMaiByCodeForCalculation(maVoucher);

            if (khuyenMaiOpt.isPresent()) {
                KhuyenMai km = khuyenMaiOpt.get();
                BigDecimal giamGiaVoucher = BigDecimal.ZERO;

                // 2a. KIỂM TRA ĐIỀU KIỆN ÁP DỤNG (Min Order Value)
                BigDecimal minOrderValue = km.getGiaTriDonHangToiThieu() != null ? km.getGiaTriDonHangToiThieu() : BigDecimal.ZERO;

                // Nếu tiền còn lại sau chiết khấu thành viên < Min Order Value của Voucher, thì không áp dụng.
                if (tienSauChietKhau.compareTo(minOrderValue) < 0) {
                    // Voucher không thỏa mãn điều kiện
                    // Bạn có thể log hoặc throw exception tùy vào yêu cầu nghiệp vụ
                    System.out.println("DEBUG: Voucher " + maVoucher + " không thỏa mãn giá trị đơn hàng tối thiểu (" + minOrderValue + ").");
                    // Chuyển sang bước 3 (nếu có) hoặc kết thúc.
                } else {

                    // 2b. TÍNH GIÁ TRỊ GIẢM
                    String loaiKm = km.getLoaiKm();
                    BigDecimal giaTri = km.getGiaTri() != null ? km.getGiaTri() : BigDecimal.ZERO;

                    if ("PERCENT".equalsIgnoreCase(loaiKm)) {

                        // i) Tính tiền giảm thô (PERCENT)
                        BigDecimal phanTram = giaTri.divide(new BigDecimal(100), 4, RoundingMode.HALF_UP);
                        giamGiaVoucher = tienSauChietKhau.multiply(phanTram);
                        giamGiaVoucher = giamGiaVoucher.setScale(0, RoundingMode.HALF_UP);

                        // ii) ÁP DỤNG MAX CAP cho Voucher PERCENT
                        BigDecimal maxCapVoucher = km.getGioiHanTienGiamToiDa() != null ? km.getGioiHanTienGiamToiDa() : BigDecimal.ZERO;

                        if (maxCapVoucher.compareTo(BigDecimal.ZERO) > 0 &&
                                giamGiaVoucher.compareTo(maxCapVoucher) > 0)
                        {
                            giamGiaVoucher = maxCapVoucher; // Áp dụng giới hạn
                        }

                    } else if ("FIXED".equalsIgnoreCase(loaiKm)) {

                        // Áp dụng giá trị cố định (FIXED)
                        giamGiaVoucher = giaTri;

                        // 🛑 LƯU Ý QUAN TRỌNG: Không để số tiền giảm FIXED lớn hơn tiền còn lại
                        if (giamGiaVoucher.compareTo(tienSauChietKhau) > 0) {
                            giamGiaVoucher = tienSauChietKhau;
                        }
                    }

                    // 2c. CỘNG VÀO TỔNG GIẢM GIÁ
                    giamGiaTong = giamGiaTong.add(giamGiaVoucher);

                    // 2d. CẬP NHẬT TIỀN CÒN LẠI (Sẵn sàng cho các bước giảm giá sau này nếu có)
                    tienSauChietKhau = tienSauChietKhau.subtract(giamGiaVoucher);
                }
            }
        }

        return giamGiaTong;
    }

    public InvoiceCalculationResult calculate(InvoiceRequest request, Map<Integer, SanPham> productMap) {

        // --- Logic Tính Tổng Tiền Gốc ---
        BigDecimal tongTienGoc = BigDecimal.ZERO;

        for (InvoiceItemDto item : request.getItems()) {
            SanPham sp = productMap.get(item.getMaSp());

            // Xử lý xác thực sản phẩm và giá
            if (sp == null) {
                throw new IllegalArgumentException("Sản phẩm có mã " + item.getMaSp() + " không tồn tại.");
            }
            BigDecimal donGia = sp.getDonGia();
            if (donGia == null || donGia.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Lỗi dữ liệu: Giá sản phẩm (" + item.getMaSp() + ") bằng 0...");
            }

            tongTienGoc = tongTienGoc.add(donGia.multiply(new BigDecimal(item.getSoLuong())));
        }

        // --- Logic Tính Giảm giá và Thành tiền ---
        BigDecimal giamGia = calculateDiscount(
                tongTienGoc,
                request.getHoa_don().getMa_kh(),
                request.getHoa_don().getMa_voucher_su_dung()
        );

        BigDecimal thanhTien = tongTienGoc.subtract(giamGia);
        thanhTien = thanhTien.setScale(0, RoundingMode.HALF_UP);

        // Xác thực Thành tiền
        if (thanhTien.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Tổng tiền cần thanh toán phải lớn hơn 0 VND.");
        }

        // Tạo một DTO để trả về nhiều giá trị
        return new InvoiceCalculationResult(tongTienGoc, giamGia, thanhTien);
    }
}