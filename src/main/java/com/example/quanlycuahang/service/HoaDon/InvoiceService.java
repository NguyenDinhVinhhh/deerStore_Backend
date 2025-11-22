package com.example.quanlycuahang.service.HoaDon;

import com.example.quanlycuahang.config.MomoConfig;
import com.example.quanlycuahang.dto.HoaDon.InvoiceHeaderDto;
import com.example.quanlycuahang.dto.HoaDon.InvoiceItemDto;
import com.example.quanlycuahang.dto.HoaDon.InvoiceRequest;
import com.example.quanlycuahang.dto.HoaDon.PaymentDto;
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
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        String initialStatus = "Hoàn thành";
        if (isOnlinePayment) {
            initialStatus = "Chờ thanh toán Online"; // 👈 Nếu là Online, luôn chờ
        } else if (tienKhachTra.compareTo(thanhTien) < 0) {
            initialStatus = "Chờ thanh toán";
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

        HoaDon hd = hoaDonRepository.findById(maHd)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy Hóa đơn ID: " + orderId));

        // 1. Xác thực Trạng thái
        if (!"Chờ thanh toán Online".equals(hd.getTrang_thai())) {
            // Có thể là IPN (Instant Payment Notification) trùng lặp, bỏ qua
            System.out.println("Cảnh báo: IPN trùng lặp hoặc trạng thái không hợp lệ cho Hóa đơn ID: " + orderId);
            return;
        }

        // 2. Xác thực Số tiền
        if (hd.getThanh_tien().compareTo(amount) != 0) {
            throw new IllegalStateException("Số tiền thanh toán (" + amount + ") không khớp với hóa đơn (" + hd.getThanh_tien() + ").");
        }

        // 3. Cập nhật Hóa đơn và Ghi Thanh toán
        hd.setTrang_thai("Hoàn thành");
        hd.setTien_khach_tra(hd.getThanh_tien());
        hoaDonRepository.save(hd);

        ThanhToan tt = new ThanhToan();
        tt.setMa_hd(maHd);
        tt.setPhuong_thuc(phuongThuc);
        tt.setSo_tien(amount);
        tt.setGhi_chu("Mã giao dịch: " + transId);
        thanhToanRepository.save(tt);

        // -----------------------------------------------------
        // 4. BỔ SUNG LOGIC NGHIỆP VỤ BỊ TRÌ HOÃN
        // -----------------------------------------------------

        // 4a. TRỪ TỒN KHO

        // Lấy chi tiết hóa đơn (cần thiết để biết sản phẩm và số lượng)
        List<ChiTietHoaDon> chiTietList = chiTietHoaDonRepository.findByMaHdQuery(maHd);

        // Chuyển đổi sang DTO tương thích với TonKhoService
        List<InvoiceItemDto> itemsToDeduct = chiTietList.stream()
                .map(ct -> new InvoiceItemDto(ct.getMa_sp(), ct.getSo_luong()))
                .collect(Collectors.toList());

        // Xác định Kho hàng
        Integer maKho = khoHangService.getMaKhoByMaChiNhanh(hd.getMa_chi_nhanh());

        // Thực hiện trừ tồn kho
        tonkhoService.deductStock(maKho, itemsToDeduct);

        // 4b. CẬP NHẬT TÍCH LŨY VÀ XÉT THĂNG HẠNG
        Integer maKhachHang = hd.getMa_kh();
        BigDecimal thanhTien = hd.getThanh_tien();

        if (maKhachHang != null) {
            // Cập nhật Tổng chi tiêu lũy kế
            KhachHang updatedKhachHang = khachHangService.updateCumulativeSpending(maKhachHang, thanhTien);

            // Xét thăng hạng
            if (updatedKhachHang != null) {
                khachHangService.checkAndUpgradeRank(updatedKhachHang);
            }
        }

        // Giao dịch kết thúc thành công.
    }



}