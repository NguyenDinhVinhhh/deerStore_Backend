package com.example.quanlycuahang.service.MOMO;

import com.example.quanlycuahang.config.MomoConfig;
import com.example.quanlycuahang.util.HashUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MomoService {

    @Autowired
    private MomoConfig momoConfig;

    /**
     * Tạo request thanh toán MoMo
     */
    public String createPaymentRequest(long amount, String orderId) {
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderInfo = "Thanh toan don hang #" + orderId;
        String extraData = "";
        String requestType = "payWithMethod"; // Hoặc "captureWallet"

        // Raw Data chuẩn theo MoMo docs
        String rawData =
                "accessKey=" + momoConfig.getAccessKey()
                        + "&amount=" + amount
                        + "&extraData=" + extraData
                        + "&ipnUrl=" + momoConfig.getIpnUrl()
                        + "&orderId=" + orderId
                        + "&orderInfo=" + orderInfo
                        + "&partnerCode=" + momoConfig.getPartnerCode()
                        + "&redirectUrl=" + momoConfig.getRedirectUrl()
                        + "&requestId=" + requestId
                        + "&requestType=" + requestType;

        String signature = HashUtils.signHmacSHA256(rawData, momoConfig.getSecretKey());

        return String.format("""
        {
            "partnerCode": "%s",
            "accessKey": "%s",
            "requestId": "%s",
            "amount": %d,
            "orderId": "%s",
            "orderInfo": "%s",
            "redirectUrl": "%s",
            "ipnUrl": "%s",
            "requestType": "%s",
            "extraData": "%s",
            "signature": "%s"
        }
        """,
                momoConfig.getPartnerCode(),
                momoConfig.getAccessKey(),
                requestId,
                amount,
                orderId,
                orderInfo,
                momoConfig.getRedirectUrl(),
                momoConfig.getIpnUrl(),
                requestType,
                extraData,
                signature
        );
    }

    /**
     * Tạo chuỗi Raw Data IPN để xác thực chữ ký
     * Lưu ý: accessKey lấy từ config, không lấy từ ipnData
     */
    public String createIpnRawData(JsonNode ipnData) {

        // Debug log
        System.out.println("===== IPN RAW BODY =====");
        System.out.println(ipnData.toPrettyString());

        String rawData = "accessKey=" + momoConfig.getAccessKey()
                + "&amount=" + ipnData.path("amount").asText()
                + "&extraData=" + ipnData.path("extraData").asText("")
                + "&message=" + ipnData.path("message").asText("")
                + "&orderId=" + ipnData.path("orderId").asText()
                + "&orderInfo=" + ipnData.path("orderInfo").asText("")
                + "&orderType=" + ipnData.path("orderType").asText("")
                + "&partnerCode=" + ipnData.path("partnerCode").asText()
                + "&payType=" + ipnData.path("payType").asText("")
                + "&requestId=" + ipnData.path("requestId").asText()
                + "&responseTime=" + ipnData.path("responseTime").asText()
                + "&resultCode=" + ipnData.path("resultCode").asText()
                + "&transId=" + ipnData.path("transId").asText();

        // Debug log
        System.out.println("===== RAW DATA BẠN TẠO =====");
        System.out.println(rawData);

        String signature = HashUtils.signHmacSHA256(rawData, momoConfig.getSecretKey());
        System.out.println("===== SIGNATURE BẠN TẠO =====");
        System.out.println(signature);

        return rawData;
    }
}
