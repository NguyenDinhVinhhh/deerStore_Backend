package com.example.quanlycuahang.controller.MOMO;

import com.example.quanlycuahang.config.MomoConfig;
import com.example.quanlycuahang.service.MOMO.MomoService;
import com.example.quanlycuahang.service.HoaDon.InvoiceService; // <<< CẦN THÊM: Import InvoiceService
import com.example.quanlycuahang.util.HashUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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


    //API: XỬ LÝ RETURN URL (HIỂN THỊ KẾT QUẢ CHO NGƯỜI DÙNG)

    @GetMapping("/return")
    public ResponseEntity<String> handleReturnUrl(
            @RequestParam int resultCode,
            @RequestParam String orderId,
            @RequestParam String message,
            @RequestParam String amount
    ) {

        String formattedAmount = "0đ";
        try {
            double amountDouble = Double.parseDouble(amount);
            java.text.NumberFormat formatter = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale("vi", "VN"));
            formattedAmount = formatter.format(amountDouble);
        } catch (Exception e) {
            formattedAmount = amount + "đ";
        }

        if (resultCode == 0) {
            String htmlResponse = "<div style='text-align: center; padding: 50px; font-family: Arial, sans-serif;'>"
                    + "<h1 style='color: #28a745;'>THANH TOÁN THÀNH CÔNG</h1>"
                    + "<p>Mã đơn hàng: <strong>" + orderId + "</strong></p>"
                    + "<p>Số tiền đã thanh toán: <strong style='color: #d33; font-size: 1.2em;'>" + formattedAmount + "</strong></p>"
                    + "<p>Trạng thái: Giao dịch của bạn đã được hệ thống ghi nhận.</p>"
                    + "<a href='/' style='display: inline-block; margin-top: 20px; padding: 10px 20px; background: #ae2070; color: #fff; text-decoration: none; border-radius: 5px;'>Quay lại cửa hàng</a>"
                    + "</div>";
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE).body(htmlResponse);
        } else {
            String htmlResponse = "<div style='text-align: center; padding: 50px; font-family: Arial, sans-serif;'>"
                    + "<h1 style='color: #dc3545;'>THANH TOÁN THẤT BẠI</h1>"
                    + "<p>Mã đơn hàng: <strong>" + orderId + "</strong></p>"
                    + "<p>Số tiền: <strong>" + formattedAmount + "</strong></p>"
                    + "<p>Lý do: " + message + " (Mã lỗi: " + resultCode + ")</p>"
                    + "<p style='color: #666;'>Vui lòng kiểm tra lại tài khoản MoMo hoặc thử phương thức khác.</p>"
                    + "<a href='/' style='display: inline-block; margin-top: 20px; padding: 10px 20px; background: #6c757d; color: #fff; text-decoration: none; border-radius: 5px;'>Thanh toán lại</a>"
                    + "</div>";
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED).header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE).body(htmlResponse);
        }
    }
}