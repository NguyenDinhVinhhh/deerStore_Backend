package com.example.quanlycuahang.controller.HoaDon;

import com.example.quanlycuahang.dto.HoaDon.InvoiceRequest;
import com.example.quanlycuahang.dto.ThongKe.BieuDoDoanhThuResponse;
import com.example.quanlycuahang.dto.ThongKe.ThoiGianThongKe;
import com.example.quanlycuahang.dto.ThongKe.ThongKeDoanhThuRequest;
import com.example.quanlycuahang.dto.ThongKe.ThongKe_Donhang_Doanhthu_Request;
import com.example.quanlycuahang.service.HoaDon.InvoiceService;
import com.example.quanlycuahang.service.MOMO.MomoService;
import com.example.quanlycuahang.config.MomoConfig;
import com.example.quanlycuahang.util.HashUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @Autowired
    private MomoService momoService;

    @Autowired
    private MomoConfig momoConfig;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // api tạo hóa đơn
    @PostMapping("/create")
    public ResponseEntity<?> createInvoice(@RequestBody InvoiceRequest request) {
        try {

            Map<String, Object> responseData = invoiceService.createInvoice(request);

            String status = (String) responseData.get("status");
            String orderId = (String) responseData.get("orderId");

            if ("Chờ thanh toán Online".equals(status)) {

                String payUrl = (String) responseData.get("payUrl");

                // Trả về JSON cho Frontend biết: cần chuyển hướng
                return ResponseEntity.ok(
                        Map.of(
                                "orderId", orderId,
                                "status", status,
                                "paymentType", responseData.get("paymentType"),
                                "payUrl", payUrl,
                                "message", "Chuyển hướng đến cổng thanh toán."
                        )
                );
            } else {

                return new ResponseEntity<>(
                        Map.of(
                                "orderId", orderId,
                                "status", status,
                                "message", "Tạo hóa đơn và thanh toán thành công."
                        ),
                        HttpStatus.CREATED
                );
            }

        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Lỗi dữ liệu đầu vào: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Lỗi xử lý giao dịch: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //api xử lý ipn trả về của momo
    @PostMapping("/momo/ipn")
    public ResponseEntity<String> handleMomoIpn(@RequestBody String ipnJson) {

        try {

            JsonNode ipnData = objectMapper.readTree(ipnJson);


            System.out.println("===== IPN RAW BODY =====");
            System.out.println(ipnJson);

            System.out.println("===== IPN JSON PARSED =====");
            System.out.println(ipnData.toPrettyString());

            String momoSignature = ipnData.path("signature").asText();
            System.out.println("===== SIGNATURE MOMO GỬI =====");
            System.out.println(momoSignature);

            String rawData = momoService.createIpnRawData(ipnData);
            System.out.println("===== RAW DATA BẠN TẠO =====");
            System.out.println(rawData);

            String mySignature = HashUtils.signHmacSHA256(rawData, momoConfig.getSecretKey());
            System.out.println("===== SIGNATURE BẠN TẠO =====");
            System.out.println(mySignature);
            // ===============================


            int resultCode = ipnData.get("resultCode").asInt();
            String orderId = ipnData.get("orderId").asText();
            String transId = ipnData.get("transId").asText();
            long amountLong = ipnData.get("amount").asLong();
            String phuongThuc = "MOMO";

            // =============================== VERIFY ===============================

            if (mySignature.equals(momoSignature)) {

                if (resultCode == 0) {

                    invoiceService.finalizeOnlinePayment(orderId, transId,
                            new BigDecimal(amountLong), phuongThuc);

                    System.out.println(" IPN SUCCESS: Hóa đơn " + orderId + " đã được hoàn tất.");
                } else {
                    System.out.println(" IPN FAILED: Hóa đơn " + orderId + " thất bại. Mã lỗi: " + resultCode);
                }

                return ResponseEntity.ok("{\"message\": \"Received IPN successfully\"}");

            } else {
                System.err.println(" LỖI BẢO MẬT: Chữ ký IPN không hợp lệ.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("{\"message\": \"Invalid signature\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Server Error\"}");
        }
    }
    // báo cáo hôm nay
    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(
            @RequestParam(required = false) Integer maChiNhanh
    ) {
        return ResponseEntity.ok(
                invoiceService.thongKeHomNay(maChiNhanh)
        );
    }
    @PostMapping("/doanh-thu")
    public ResponseEntity<BigDecimal> getDoanhThu(
            @RequestBody ThongKeDoanhThuRequest request
    ) {
        BigDecimal doanhThu = invoiceService.getDoanhThuTheoThoiGian(
                request.getKieuThoiGian(),
                request.getMaChiNhanh()
        );
        return ResponseEntity.ok(doanhThu);
    }

    // api danh cho biểu đồ báo cáo
    @PostMapping("/doanh-thu/bieu-do")
    public ResponseEntity<List<BieuDoDoanhThuResponse>> getBieuDoDoanhThu(
            @RequestBody ThongKeDoanhThuRequest request
    ) {
        return ResponseEntity.ok(
                invoiceService.getBieuDoDoanhThu(request)
        );
    }

    @GetMapping("/doanh-thu/so-sanh-chi-nhanh")
    public ResponseEntity<?> soSanhDoanhThuChiNhanh(
            @RequestParam ThoiGianThongKe thoiGian
    ) {
        return ResponseEntity.ok(
                invoiceService.soSanhDoanhThuChiNhanh(thoiGian)
        );
    }

    /// api lấy danh sách hóa đơn
    @GetMapping
    public ResponseEntity<?> getDanhSachHoaDon(
            @RequestParam(required = false) Integer maChiNhanh,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) LocalDateTime start,
            @RequestParam(required = false) LocalDateTime end,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                invoiceService.getDanhSachHoaDon(
                        maChiNhanh,
                        trangThai,
                        start,
                        end,
                        page,
                        size
                )
        );
    }
}