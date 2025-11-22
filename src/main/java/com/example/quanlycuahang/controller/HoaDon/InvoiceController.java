package com.example.quanlycuahang.controller.HoaDon;

import com.example.quanlycuahang.dto.HoaDon.InvoiceRequest;
import com.example.quanlycuahang.service.HoaDon.InvoiceService;
import com.example.quanlycuahang.service.MOMO.MomoService;
import com.example.quanlycuahang.config.MomoConfig;
import com.example.quanlycuahang.util.HashUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
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
            // 1. Gọi Service để xử lý tạo hóa đơn, trừ tồn kho và xác định trạng thái
            // Service trả về Map chứa orderId, status và payUrl (nếu là MoMo)
            Map<String, Object> responseData = invoiceService.createInvoice(request);

            String status = (String) responseData.get("status");
            String orderId = (String) responseData.get("orderId");

            if ("Chờ thanh toán Online".equals(status)) {
                // 2. Nếu là thanh toán Online (ví dụ: MoMo), trả về payUrl
                // Logic gọi API MoMo đã được chuyển vào InvoiceService (BƯỚC NÀY KHÔNG CẦN GỌI LẠI MOMO)

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
                // 3. Nếu là Tiền mặt/Thẻ/Chuyển khoản (Hoàn thành ngay)

                // Trả về thông báo thành công cho Frontend
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

    //--------------------------------------------------------------------------
    // 2. API: XỬ LÝ IPN (INSTANT PAYMENT NOTIFICATION) TỪ MO MO
    // Endpoint bảo mật để xác nhận và hoàn tất giao dịch.
    //--------------------------------------------------------------------------

    @PostMapping("/momo/ipn")
    public ResponseEntity<String> handleMomoIpn(@RequestBody String ipnJson) {

        try {
            // Parse JSON
            JsonNode ipnData = objectMapper.readTree(ipnJson);

            // ========== DEBUG FULL ==========
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

            // Lấy dữ liệu quan trọng
            int resultCode = ipnData.get("resultCode").asInt();
            String orderId = ipnData.get("orderId").asText();
            String transId = ipnData.get("transId").asText();
            long amountLong = ipnData.get("amount").asLong();
            String phuongThuc = "MOMO";

            // =============================== VERIFY ===============================

            if (mySignature.equals(momoSignature)) {

                if (resultCode == 0) {
                    // Giao dịch thành công → cập nhật hóa đơn
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

}