package com.example.quanlycuahang.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    // Khởi tạo một Key an toàn đạt chuẩn 512-bit cho thuật toán HS512
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final long jwtExpirationMs = 86400000; // 1 ngày

    // ✅ Hàm tạo Token: Nạp quyền (authorities) vào chuỗi JWT
    public String generateJwtToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim("authorities", authorities) // Lưu danh sách quyền vào Token
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(secretKey)
                .compact();
    }

    // ✅ Hàm giải mã: Lấy Username từ Token
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // ✅ Hàm kiểm tra Token hợp lệ
    public boolean validateToken(String token, String username) {
        try {
            String tokenUsername = extractUsername(token);
            return (tokenUsername != null && tokenUsername.equals(username) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }

    // ✅ Hàm kiểm tra hạn sử dụng Token
    private boolean isTokenExpired(String token) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }

    // ✅ QUAN TRỌNG: Cung cấp Key để các lớp khác (Filter) dùng chung khi giải mã
    public SecretKey getSecretKey() {
        return this.secretKey;
    }

    // ✅ Thêm hàm này vào JwtUtil.java
    public String generateTokenForCustomer(String username) {
        return Jwts.builder()
                .setSubject(username)
                .claim("authorities", "ROLE_CUSTOMER") // Gán quyền mặc định đơn giản
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(secretKey)
                .compact();
    }
}