package com.example.quanlycuahang.controller.Auth;

import com.example.quanlycuahang.dto.Auth.*;
import com.example.quanlycuahang.dto.ChiNhanh.ChiNhanhDto;
import com.example.quanlycuahang.entity.TaiKhoan.TaiKhoan;
import com.example.quanlycuahang.service.Auth.AuthService;
import com.example.quanlycuahang.service.ChiNhanh.BranchService;
import com.example.quanlycuahang.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    // ✅ Thống nhất dùng JwtUtil để tạo Token có chứa quyền (authorities)
    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private BranchService branchService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginRequest request) {
        try {
            // 1. Xác thực bằng AuthenticationManager (Spring sẽ nạp quyền từ Database vào đây)
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getTenDangNhap(), request.getMatKhau())
            );

            // 2. Dùng JwtUtil để tạo Token. Hàm này sẽ lấy danh sách quyền từ object authentication
            String jwtToken = jwtUtils.generateJwtToken(authentication);

            TaiKhoan taiKhoan = (TaiKhoan) authentication.getPrincipal();
            List<ChiNhanhDto> branches = branchService.getBranchesByMaTk(taiKhoan.getMaTk());

            AuthResponse response = AuthResponse.builder()
                    .token(jwtToken)
                    .hoTen(taiKhoan.getHoTen())
                    .maTk(taiKhoan.getMaTk())
                    .isSuperAdmin(taiKhoan.getIsSuperAdmin())
                    .maVaiTro(taiKhoan.getMaVaiTro())
                    .chiNhanhList(branches)
                    .build();

            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Tên đăng nhập hoặc mật khẩu không đúng."));
        }
    }

    @PostMapping("/register/staff")
    public ResponseEntity<?> registerStaff(@RequestBody StaffRegisterRequest request) {
        try {
            if (request.getDanhSachChiNhanh() == null || request.getDanhSachChiNhanh().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("error", "Phải chọn ít nhất một chi nhánh cho tài khoản."));
            }

            TaiKhoan newAccount = authService.registerStaff(request);

            return ResponseEntity.ok(Collections.singletonMap(
                    "message",
                    "Tạo tài khoản '" + newAccount.getTenDangNhap() + "' thành công và đã được gán chi nhánh."
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("error", "Đã xảy ra lỗi trong quá trình tạo tài khoản."));
        }
    }

    @GetMapping("/tai-khoan")
    public ResponseEntity<List<TaiKhoanDto>> getAllTaiKhoan() {
        List<TaiKhoanDto> taiKhoanList = authService.getAllTaiKhoanDto();
        return ResponseEntity.ok(taiKhoanList);
    }

    @PutMapping("/tai-khoan/{maTk}")
    public ResponseEntity<?> updateTaiKhoan(
            @PathVariable Integer maTk,
            @RequestBody UpdateTaiKhoanRequest request
    ) {
        try {
            authService.updateTaiKhoan(maTk, request);
            return ResponseEntity.ok(Collections.singletonMap("message", "Cập nhật tài khoản thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

    @GetMapping("/tai-khoan/search")
    public ResponseEntity<List<TaiKhoanDto>> searchTaiKhoan(@RequestParam String hoTen) {
        List<TaiKhoanDto> result = authService.searchTaiKhoanByHoTen(hoTen);
        return ResponseEntity.ok(result);
    }
}