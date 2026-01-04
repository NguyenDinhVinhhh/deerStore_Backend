package com.example.quanlycuahang.service.HoaDon;

import com.example.quanlycuahang.config.MomoConfig;
import com.example.quanlycuahang.dto.HoaDon.*;
import com.example.quanlycuahang.dto.ThongKe.*;
import com.example.quanlycuahang.dto.TonKho.InvoiceCalculationResult;
import com.example.quanlycuahang.entity.HoaDon.ChiTietHoaDon;
import com.example.quanlycuahang.entity.HoaDon.HoaDon;
import com.example.quanlycuahang.entity.KhachHang.KhachHang;
import com.example.quanlycuahang.entity.KhoHang.KhoHang;
import com.example.quanlycuahang.entity.SanPham.SanPham;
import com.example.quanlycuahang.entity.ThanhToan.ThanhToan;

import com.example.quanlycuahang.repository.HoaDon.ChiTietHoaDonRepository;
import com.example.quanlycuahang.repository.HoaDon.HoaDonRepository;
import com.example.quanlycuahang.repository.KhachHang.KhachHangRepository;
import com.example.quanlycuahang.repository.KhoHang.KhoHangRepository;
import com.example.quanlycuahang.repository.SanPham.SanPhamRepository;
import com.example.quanlycuahang.repository.ThanhToan.ThanhToanRepository;
import com.example.quanlycuahang.repository.TonKho.TonKhoRepository;
import com.example.quanlycuahang.service.KhachHang.KhachHangService;
import com.example.quanlycuahang.service.KhoHang.KhoHangService;
import com.example.quanlycuahang.service.MOMO.MomoPaymentService;
import com.example.quanlycuahang.service.MOMO.MomoService;
import com.example.quanlycuahang.service.TonKho.TonKhoService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InvoiceService {

    @Autowired private HoaDonRepository hoaDonRepository;
    @Autowired private ChiTietHoaDonRepository chiTietHoaDonRepository;
    @Autowired private SanPhamRepository sanPhamRepository;
    @Autowired private ThanhToanRepository thanhToanRepository;
    @Autowired private TonKhoRepository tonKhoRepository;
    @Autowired private KhoHangRepository khoHangRepository;
    @Autowired private MomoService momoService;
    @Autowired private MomoConfig momoConfig;
    @Autowired private KhoHangService khoHangService;
    @Autowired private InvoiceCalculationService calculationService;
    @Autowired private TonKhoService tonkhoService;
    @Autowired private MomoPaymentService momoPaymentService;
    @Autowired private KhachHangService khachHangService;
    @Autowired private KhachHangRepository khachHangRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final List<String> ONLINE_PAYMENT_METHODS = List.of("MOMO", "VNPAY");


    // hàm tạo hóa đơn xử lý thanh toán chứa thông tin phản hồi (Status, OrderId, PaymentUrl nếu có).
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> createInvoice(InvoiceRequest request) throws Exception {

        // 1. Tải SanPham và tạo Map
        List<Integer> maSpList = request.getItems().stream().map(InvoiceItemDto::getMaSp).collect(Collectors.toList());
        List<SanPham> products = sanPhamRepository.findFullProductsByIds(maSpList);
        Map<Integer, SanPham> productMap = products.stream().collect(Collectors.toMap(SanPham::getMaSp, p -> p));

        // 2. TÍNH TOÁN
        InvoiceCalculationResult result = calculationService.calculate(request, productMap);
        BigDecimal tongTienGoc = result.tongTienGoc();
        BigDecimal giamGia = result.giamGia();
        BigDecimal thanhTien = result.thanhTien(); // Giá trị cuối cùng cần thanh toán

        // 3. Xác thực Thanh toán Online/Offline
        boolean isOnlinePayment = request.getPayment().stream()
                .anyMatch(p -> ONLINE_PAYMENT_METHODS.contains(p.getPhuong_thuc().toUpperCase()));

        // Kiểm tra giới hạn tối thiểu của MoMo
        if (isOnlinePayment && thanhTien.compareTo(new BigDecimal(1000)) < 0) {
            throw new IllegalArgumentException("Thanh toán Online cần tối thiểu 1.000 VND.");
        }

        // 4. Xác định Kho hàng
        Integer maChiNhanh = request.getHoa_don().getMa_chi_nhanh();
        Integer maKho = khoHangService.getMaKhoByMaChiNhanh(maChiNhanh);


        // 5. Tính Tiền khách trả (Chỉ cho Offline)
        BigDecimal tienKhachTra = BigDecimal.ZERO;
        if (!isOnlinePayment) {
            tienKhachTra = request.getPayment().stream()
                    .map(PaymentDto::getSo_tien)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(0, RoundingMode.HALF_UP);
        }

        // 6. GHI VÀO BẢNG `hoa_don`
        InvoiceHeaderDto hoaDonHeaderDto = request.getHoa_don(); // 💡 Đã sửa tên biến
        HoaDon hd = new HoaDon();

        // Mapping các trường từ DTO
        hd.setMa_tk(hoaDonHeaderDto.getMa_tk());
        hd.setMa_chi_nhanh(hoaDonHeaderDto.getMa_chi_nhanh());
        hd.setMa_kh(hoaDonHeaderDto.getMa_kh());
        hd.setMa_km(hoaDonHeaderDto.getMa_km());
        hd.setMa_voucher_su_dung(hoaDonHeaderDto.getMa_voucher_su_dung());
        hd.setGhi_chu(hoaDonHeaderDto.getGhi_chu());

        // Mapping các trường tính toán
        hd.setTong_tien(tongTienGoc.setScale(0, RoundingMode.HALF_UP));
        hd.setGiam_gia(giamGia.setScale(0, RoundingMode.HALF_UP));
        hd.setThanh_tien(thanhTien);
        hd.setTien_khach_tra(tienKhachTra);

        // Xác định trạng thái ban đầu
        String initialStatus;

        if (tienKhachTra != null && tienKhachTra.compareTo(thanhTien) >= 0) {
            // Đã thanh toán đủ (tiền mặt hoặc online thành công)
            initialStatus = "HOAN THANH";
        } else {
            // Chưa thanh toán hoặc chờ thanh toán online
            initialStatus = "CHO THANH TOAN";
        }

        hd.setTrang_thai(initialStatus);


        hd = hoaDonRepository.save(hd);


        // 7. GHI CHI TIẾT HÓA ĐƠN
        for (InvoiceItemDto item : request.getItems()) {
            SanPham sp = productMap.get(item.getMaSp());

            ChiTietHoaDon cthd = new ChiTietHoaDon();
            cthd.setMa_hd(hd.getMa_hd());
            cthd.setMa_sp(item.getMaSp());
            cthd.setSo_luong(item.getSoLuong());
            cthd.setDon_gia(sp.getDonGia().setScale(0, RoundingMode.HALF_UP));
            chiTietHoaDonRepository.save(cthd);
        }

        // 8. LOGIC CHỈ DÀNH CHO OFFLINE (Hoàn thành ngay)
        if (!isOnlinePayment) {

            // 8a. TRỪ TỒN KHO (CHỈ KHI OFFLINE)
            // 🛑 Lỗi cũ: Logic này phải nằm trong khối IF (!isOnlinePayment)
            tonkhoService.deductStock(maKho, request.getItems());

            // 8b. GHI VÀO BẢNG `thanh_toan` (CHỈ KHI OFFLINE)
            for (PaymentDto pmt : request.getPayment()) {
                ThanhToan tt = new ThanhToan();
                tt.setMa_hd(hd.getMa_hd());
                tt.setPhuong_thuc(pmt.getPhuong_thuc());
                tt.setSo_tien(pmt.getSo_tien().setScale(0, RoundingMode.HALF_UP));
                tt.setGhi_chu(pmt.getGhi_chu());
                thanhToanRepository.save(tt);
            }

            // 8c. CẬP NHẬT TÍCH LŨY VÀ XÉT THĂNG HẠNG (CHỈ KHI OFFLINE)
            Integer maKhachHang = hd.getMa_kh();
            if (maKhachHang != null) {
                KhachHang updatedKhachHang = khachHangService.updateCumulativeSpending(maKhachHang, thanhTien);
                if (updatedKhachHang != null) {
                    khachHangService.checkAndUpgradeRank(updatedKhachHang);
                }
            }
        }


        // 9. XỬ LÝ PHẢN HỒI VÀ GỌI CỔNG THANH TOÁN

        Map<String, Object> responseData = Map.of(
                "orderId", String.valueOf(hd.getMa_hd()),
                "status", hd.getTrang_thai(),
                "message", "Tạo đơn thành công." // Default message
        );

        if (isOnlinePayment) {
            // 9a. Gọi Service Online Payment
            PaymentDto onlinePayment = request.getPayment().stream()
                    .filter(p -> ONLINE_PAYMENT_METHODS.contains(p.getPhuong_thuc().toUpperCase()))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("Lỗi logic: Không tìm thấy phương thức thanh toán online."));

            if ("MOMO".equals(onlinePayment.getPhuong_thuc().toUpperCase())) {
                // GỌI SERVICE CHUYÊN TRÁCH ĐÃ TÁCH
                responseData = momoPaymentService.initiateMomoPayment(
                        String.valueOf(hd.getMa_hd()),
                        thanhTien
                );
            }
        }

        return responseData;
    }

    //-------------------------------------------------------------------------
    // 2. XỬ LÝ HOÀN THÀNH THANH TOÁN ONLINE (Gọi từ IPN Controller)
    //-------------------------------------------------------------------------

    @Transactional(rollbackFor = Exception.class)
    public void finalizeOnlinePayment(String orderId, String transId, BigDecimal amount, String phuongThuc) throws Exception {
        int maHd = Integer.parseInt(orderId);

        // 1. Tìm hóa đơn trong Database
        HoaDon hd = hoaDonRepository.findById(maHd)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Hóa đơn ID: " + orderId));

        // 2. Xác thực trạng thái - Tránh xử lý trùng lặp nếu MoMo gửi IPN nhiều lần
        if (!"CHO THANH TOAN".equals(hd.getTrang_thai())) {
            System.out.println("Cảnh báo: IPN trùng lặp cho Hóa đơn ID: " + orderId);
            return;
        }

        // 3. Xác thực số tiền thanh toán khớp với hóa đơn
        if (hd.getThanh_tien().compareTo(amount) != 0) {
            throw new IllegalStateException("Số tiền MoMo trả về (" + amount + ") không khớp với hóa đơn (" + hd.getThanh_tien() + ").");
        }

        // 4. Cập nhật trạng thái Hóa đơn và ghi nhận số tiền khách trả
        hd.setTrang_thai("HOAN THANH");
        hd.setTien_khach_tra(hd.getThanh_tien());
        hoaDonRepository.save(hd);

        // 5. Ghi lịch sử vào bảng ThanhToan
        ThanhToan tt = new ThanhToan();
        tt.setMa_hd(maHd);
        tt.setPhuong_thuc(phuongThuc);
        tt.setSo_tien(amount);
        tt.setGhi_chu("Thanh toán Online thành công. Mã giao dịch: " + transId);
        thanhToanRepository.save(tt);

        // 6. Thực hiện trừ tồn kho (Dựa trên chi tiết hóa đơn đã lưu)
        List<ChiTietHoaDon> chiTietList = chiTietHoaDonRepository.findByMaHdQuery(maHd);
        List<InvoiceItemDto> itemsToDeduct = chiTietList.stream()
                .map(ct -> new InvoiceItemDto(ct.getMa_sp(), ct.getSo_luong()))
                .collect(Collectors.toList());

        Integer maKho = khoHangService.getMaKhoByMaChiNhanh(hd.getMa_chi_nhanh());
        tonkhoService.deductStock(maKho, itemsToDeduct);


        Integer maKhachHang = hd.getMa_kh();
        if (maKhachHang != null) {
            KhachHang updatedKh = khachHangService.updateCumulativeSpending(maKhachHang, hd.getThanh_tien());
            if (updatedKh != null) {
                khachHangService.checkAndUpgradeRank(updatedKh);
            }
        }

        System.out.println("Thanh toán thành công và hoàn tất nghiệp vụ cho Hóa đơn: " + maHd);
    }

    public ThongKe_Donhang_Doanhthu_Request thongKeHomNay(Integer maChiNhanh) {

        LocalDateTime start = LocalDate.now().atStartOfDay();
        LocalDateTime end = LocalDate.now().atTime(23, 59, 59);

        ThongKe_Donhang_Doanhthu_Request res = new ThongKe_Donhang_Doanhthu_Request();
        res.setMaChiNhanh(maChiNhanh);
        res.setTongDonHangMoi(
                hoaDonRepository.countDonHangMoiHomNay(start, end, maChiNhanh)
        );
        res.setTongDoanhThu(
                hoaDonRepository.tongDoanhThuHomNay(start, end, maChiNhanh)
        );
        res.setTongDonTraHang(
                hoaDonRepository.countDonTraHang(start, end, maChiNhanh)
        );
        res.setTongDonHuy(
                hoaDonRepository.countDonHuy(start, end, maChiNhanh)
        );


        return res;
    }

    private LocalDateTime[] resolveTimeRange(ThoiGianThongKe type) {
        LocalDate today = LocalDate.now();

        return switch (type) {
            case HOM_NAY -> new LocalDateTime[]{
                    today.atStartOfDay(),
                    today.atTime(23, 59, 59)
            };

            case HOM_QUA -> new LocalDateTime[]{
                    today.minusDays(1).atStartOfDay(),
                    today.minusDays(1).atTime(23, 59, 59)
            };

            case BAY_NGAY_QUA -> new LocalDateTime[]{
                    today.minusDays(6).atStartOfDay(),
                    today.atTime(23, 59, 59)
            };

            case THANG_NAY -> new LocalDateTime[]{
                    today.withDayOfMonth(1).atStartOfDay(),
                    today.atTime(23, 59, 59)
            };

            case NAM_NAY -> new LocalDateTime[]{
                    today.withDayOfYear(1).atStartOfDay(),
                    today.atTime(23, 59, 59)
            };

            default -> throw new IllegalArgumentException("Không hỗ trợ kiểu thời gian");
        };
    }
    @Transactional(readOnly = true)
    public BigDecimal getDoanhThuTheoThoiGian(
            ThoiGianThongKe type,
            Integer maChiNhanh
    ) {
        LocalDateTime[] range = resolveTimeRange(type);
        return hoaDonRepository.tongDoanhThuHomNay(
                range[0],
                range[1],
                maChiNhanh
        );
    }


    // hàm biểu đồ doanh thu
    @Transactional(readOnly = true)
    public List<BieuDoDoanhThuResponse> getBieuDoDoanhThu(
            ThongKeDoanhThuRequest request
    ) {
        ThoiGianThongKe type = request.getKieuThoiGian();
        Integer maChiNhanh = request.getMaChiNhanh();
        LocalDate today = LocalDate.now();

        // 🔹 NĂM → GROUP BY THÁNG
        if (type == ThoiGianThongKe.NAM_NAY) {
            List<Object[]> raw = hoaDonRepository.doanhThuTheoThang(
                    today.getYear(),
                    maChiNhanh
            );

            return raw.stream()
                    .map(r -> new BieuDoDoanhThuResponse(
                            "Tháng " + r[0],
                            (BigDecimal) r[1]
                    ))
                    .toList();
        }

        // 🔹 CÒN LẠI → GROUP BY NGÀY
        LocalDateTime[] range = resolveTimeRange(type);

        List<Object[]> raw = hoaDonRepository.doanhThuTheoNgay(
                range[0],
                range[1],
                maChiNhanh
        );

        return raw.stream()
                .map(r -> new BieuDoDoanhThuResponse(
                        r[0].toString(), // yyyy-MM-dd
                        (BigDecimal) r[1]
                ))
                .toList();
    }

    public List<DoanhThuChiNhanhDTO> soSanhDoanhThuChiNhanh(ThoiGianThongKe type) {

        LocalDateTime start;
        LocalDateTime end = LocalDateTime.now();

        switch (type) {
            case HOM_NAY -> start = LocalDate.now().atStartOfDay();

            case HOM_QUA -> {
                start = LocalDate.now().minusDays(1).atStartOfDay();
                end = LocalDate.now().atStartOfDay();
            }

            case BAY_NGAY_QUA ->
                    start = LocalDate.now().minusDays(6).atStartOfDay();

            case THANG_NAY ->
                    start = LocalDate.now().withDayOfMonth(1).atStartOfDay();

            case NAM_NAY ->
                    start = LocalDate.now().withDayOfYear(1).atStartOfDay();

            default ->
                    throw new IllegalArgumentException("Loại thời gian không hợp lệ");
        }

        return hoaDonRepository.thongKeDoanhThuTheoChiNhanh(start, end);
    }

    public Page<HoaDonResponse> getDanhSachHoaDon(
            Integer maChiNhanh,
            String trangThai,
            LocalDateTime start,
            LocalDateTime end,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return hoaDonRepository.getDanhSachHoaDon(
                maChiNhanh,
                trangThai,
                start,
                end,
                pageable
        );
    }

}