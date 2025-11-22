package com.example.quanlycuahang.security;


import com.example.quanlycuahang.dto.Auth.CustomUserDetailsService;
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
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService; // Sử dụng CustomUserDetailsService đã tạo

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String tenDangNhap;

        // 1. Kiểm tra header: Không có hoặc không bắt đầu bằng "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Trích xuất JWT (Bỏ "Bearer ")
        jwt = authHeader.substring(7);

        // 3. Trích xuất Tên đăng nhập từ Token
        tenDangNhap = jwtService.extractUsername(jwt);

        // 4. Kiểm tra người dùng và Security Context (Đảm bảo người dùng chưa được xác thực)
        if (tenDangNhap != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 5. Tải thông tin người dùng từ CSDL
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(tenDangNhap);

            // 6. Kiểm tra Token hợp lệ (chữ ký và hạn sử dụng)
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 7. Thiết lập đối tượng xác thực
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // Credentials là null vì đã xác thực qua Token
                        userDetails.getAuthorities() // Lấy quyền từ UserDetails
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 8. Lưu đối tượng xác thực vào Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Chuyển request đến Controller hoặc Filter tiếp theo
        filterChain.doFilter(request, response);
    }
}