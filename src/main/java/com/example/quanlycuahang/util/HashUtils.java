package com.example.quanlycuahang.util;

import org.apache.commons.codec.binary.Hex;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class HashUtils {

    /**
     * Tạo chữ ký SHA256 với HMAC (sử dụng SecretKey làm khóa)
     */
    public static String signHmacSHA256(String data, String secretKey) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256_HMAC.init(secret_key);

            // Thực hiện hash và chuyển sang chuỗi Hex
            return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));

        } catch (Exception e) {
            // Xử lý lỗi nếu cần
            e.printStackTrace();
            return null;
        }
    }
}