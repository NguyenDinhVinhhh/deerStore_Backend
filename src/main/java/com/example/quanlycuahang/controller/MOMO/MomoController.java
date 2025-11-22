package com.example.quanlycuahang.controller.MOMO;

import com.example.quanlycuahang.config.MomoConfig;
import com.example.quanlycuahang.service.MOMO.MomoService;
import com.example.quanlycuahang.service.HoaDon.InvoiceService; // <<< CẦN THÊM: Import InvoiceService
import com.example.quanlycuahang.util.HashUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/momo")
public class MomoController {

    // Inject Service cần thiết
    @Autowired private MomoService momoService;
    @Autowired private MomoConfig momoConfig;
    @Autowired private InvoiceService invoiceService; // <<< THÊM: Để gọi hàm cập nhật trạng thái

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ❌ XÓA HÀM @GetMapping("/pay")
    // Lý do: Logic tạo thanh toán đã được chuyển sang InvoiceController.create

    //--------------------------------------------------------------------------
    // 1. API: XỬ LÝ IPN (INSTANT PAYMENT NOTIFICATION) TỪ MO MO
    //--------------------------------------------------------------------------

//    @PostMapping("/ipn")
//    public ResponseEntity<String> handleIpn(@RequestBody String ipnJson) {
//        try {
//            JsonNode ipnData = objectMapper.readTree(ipnJson);
//
//            // 1. Lấy tham số
//            String signatureFromMoMo = ipnData.get("signature").asText();
//            int resultCode = ipnData.get("resultCode").asInt();
//            String orderId = ipnData.get("orderId").asText();
//            String transId = ipnData.get("transId").asText();
//            long amountLong = ipnData.get("amount").asLong();
//            String phuongThuc = "MOMO"; // Xác định rõ phương thức
//
//            // 2. Tái tạo chuỗi rawData và chữ ký
//            // Gọi hàm từ MomoService để tái tạo chuỗi IPN (Đúng format)
//            String rawData = momoService.createIpnRawData(ipnData);
//            String mySignature = HashUtils.signHmacSHA256(rawData, momoConfig.getSecretKey());
//
//            if (mySignature.equals(signatureFromMoMo)) {
//                // Chữ ký khớp: Giao dịch HỢP LỆ
//                if (resultCode == 0) {
//                    // Giao dịch THÀNH CÔNG -> HOÀN TẤT HÓA ĐƠN
//                    BigDecimal amount = new BigDecimal(amountLong);
//                    // <<< SỬA ĐỔI QUAN TRỌNG: GỌI INVOICE SERVICE >>>
//                    invoiceService.finalizeOnlinePayment(orderId, transId, amount, phuongThuc);
//                    System.out.println(" IPN SUCCESS: Đơn hàng " + orderId + " đã được hoàn tất.");
//                } else {
//                    // Giao dịch THẤT BẠI: Cần log hoặc cập nhật trạng thái thất bại (nếu cần)
//                    System.out.println(" IPN FAILED: Đơn hàng " + orderId + " thanh toán thất bại. Mã lỗi: " + resultCode);
//                }
//
//                // MoMo yêu cầu phản hồi JSON
//                return ResponseEntity.ok("{\"message\": \"Received IPN successfully\"}");
//
//            } else {
//                // Lỗi bảo mật
//                System.err.println(" LỖI BẢO MẬT: Chữ ký IPN không hợp lệ.");
//                return ResponseEntity.badRequest().body("{\"message\": \"Invalid Signature\"}");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body("{\"message\": \"Server Error\"}");
//        }
//    }

    //--------------------------------------------------------------------------
    // 2. API: XỬ LÝ RETURN URL (HIỂN THỊ KẾT QUẢ CHO NGƯỜI DÙNG)
    //--------------------------------------------------------------------------

    @GetMapping("/return")
    public ResponseEntity<String> handleReturnUrl(
            @RequestParam int resultCode,
            @RequestParam String orderId,
            @RequestParam String message
            /* ... và các tham số khác nếu cần hiển thị */
    ) {
        // Hàm này chỉ mang tính chất thông báo kết quả cho người dùng cuối
        // Việc cập nhật trạng thái đã được xử lý bởi IPN ở trên

        if (resultCode == 0) {
            String htmlResponse = "<h1> THANH TOÁN THÀNH CÔNG</h1>"
                    + "<p>Mã đơn hàng: <strong>" + orderId + "</strong></p>"
                    + "<p>Trạng thái: Đơn hàng của bạn đã được thanh toán thành công.</p>";
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE).body(htmlResponse);
        } else {
            String htmlResponse = "<h1> THANH TOÁN THẤT BẠI</h1>"
                    + "<p>Mã đơn hàng: <strong>" + orderId + "</strong></p>"
                    + "<p>Lý do: " + message + " (Mã lỗi: " + resultCode + ")</p>"
                    + "<p>Vui lòng thử lại.</p>";
            return ResponseEntity.badRequest().header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE).body(htmlResponse);
        }
    }
}