package com.example.quanlycuahang.security;

import com.example.quanlycuahang.dto.Auth.CustomUserDetailsService;
import com.example.quanlycuahang.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // ✅ Đổi từ JwtService sang JwtUtil để đồng bộ Key

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String tenDangNhap;

        String path = request.getRequestURI();



        // 1. Kiểm tra header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Trích xuất JWT
        jwt = authHeader.substring(7);

        try {
            // 3. Trích xuất Username bằng JwtUtil mới
            tenDangNhap = jwtUtil.extractUsername(jwt);

            // 4. Nếu có username và chưa được xác thực trong phiên này
            if (tenDangNhap != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 5. Tải thông tin chi tiết người dùng từ DB (bao gồm các quyền mới nhất)
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(tenDangNhap);

                // 6. Kiểm tra tính hợp lệ của Token bằng JwtUtil
                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {

                    // 7. Tạo đối tượng xác thực nạp các quyền (authorities) vào SecurityContext
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 8. Lưu vào hệ thống để các tầng sau (như Controller) nhận diện được quyền
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Nếu Token lỗi hoặc không hợp lệ, không nạp Authentication (Spring sẽ tự báo 401/403 sau đó)
            logger.error("Could not set user authentication in security context", e);
        }

        filterChain.doFilter(request, response);
    }
}