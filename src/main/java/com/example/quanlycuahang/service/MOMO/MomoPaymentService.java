package com.example.quanlycuahang.service.MOMO;


import com.example.quanlycuahang.config.MomoConfig;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class MomoPaymentService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MomoConfig momoConfig;

    @Autowired
    private MomoService momoService; // Dùng Service bạn vừa tạo

    /**
     * Khởi tạo yêu cầu thanh toán MoMo và xử lý phản hồi API.
     * @param hdMaHd Mã hóa đơn (Order ID)
     * @param thanhTien Tổng tiền cần thanh toán
     * @return Map chứa payUrl và thông tin phản hồi
     * @throws Exception nếu có lỗi trong quá trình giao tiếp hoặc lỗi từ cổng MoMo
     */
    public Map<String, Object> initiateMomoPayment(String hdMaHd, BigDecimal thanhTien) throws Exception {

        long amountForMomo = thanhTien.toBigInteger().longValue();

        // 1. Tạo JSON Request (Từ MomoService đã tách)
        String jsonRequest = momoService.createPaymentRequest(amountForMomo, hdMaHd);

        // 2. Gửi POST Request đến MoMo Sandbox API (Logic HTTP)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                momoConfig.getRequestUrl(),
                HttpMethod.POST,
                entity,
                String.class
        );

        // 3. Phân tích phản hồi từ MoMo
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode responseNode = objectMapper.readTree(response.getBody());
            int resultCode = responseNode.get("resultCode").asInt();

            if (resultCode == 0) {
                String payUrl = responseNode.get("payUrl").asText();

                return Map.of(
                        "orderId", hdMaHd,
                        "status", "Chờ thanh toán Online",
                        "paymentType", "MOMO",
                        "payUrl", payUrl,
                        "message", "Chuyển hướng đến cổng thanh toán."
                );
            } else {
                String message = responseNode.get("message").asText();
                // Ném lỗi để kích hoạt ROLLBACK trong InvoiceService
                throw new Exception("Lỗi cổng MoMo (resultCode: " + resultCode + "): " + message);
            }
        } else {
            // Ném lỗi nếu kết nối API thất bại
            throw new Exception("Lỗi kết nối API MoMo: " + response.getStatusCode());
        }
    }
}