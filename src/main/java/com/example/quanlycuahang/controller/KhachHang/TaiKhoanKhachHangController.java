package com.example.quanlycuahang.controller.KhachHang;

import com.example.quanlycuahang.dto.KhachHang.DangKyKhachHangRequest;
import com.example.quanlycuahang.dto.KhachHang.DangNhapKhachHangRequest;
import com.example.quanlycuahang.dto.KhachHang.TaiKhoanKhachHangReponse;
import com.example.quanlycuahang.entity.TaiKhoanKhachHang.TaiKhoanKhachHang;
import com.example.quanlycuahang.service.KhachHang.TaiKhoanKhachHangService;
import com.example.quanlycuahang.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/auth/customer")
public class TaiKhoanKhachHangController {

    @Autowired
    private TaiKhoanKhachHangService taiKhoanKhachHangService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody DangKyKhachHangRequest registerRequest) {
        try {
            TaiKhoanKhachHang newAccount = taiKhoanKhachHangService.register(registerRequest);

            // Sinh token đơn giản bằng username
            String token = jwtUtil.generateTokenForCustomer(newAccount.getTenDangNhap());

            return ResponseEntity.status(HttpStatus.CREATED).body(new TaiKhoanKhachHangReponse(
                    newAccount.getMaKh(),
                    newAccount.getTenDangNhap(),
                    token,
                    "Đăng ký thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@Valid @RequestBody DangNhapKhachHangRequest login) {
        try {
            TaiKhoanKhachHang loggedInAccount = taiKhoanKhachHangService.login(login);

            // Sinh token đơn giản bằng username
            String token = jwtUtil.generateTokenForCustomer(loggedInAccount.getTenDangNhap());

            return ResponseEntity.ok(new TaiKhoanKhachHangReponse(
                    loggedInAccount.getMaKh(),
                    loggedInAccount.getTenDangNhap(),
                    token,
                    "Đăng nhập thành công"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}