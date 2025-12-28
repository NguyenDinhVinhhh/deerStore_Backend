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
@RequestMapping("/api/auth/customer") // Endpoint cơ sở cho Khách hàng
public class TaiKhoanKhachHangController {

    @Autowired
    private TaiKhoanKhachHangService taiKhoanKhachHangService;
    @Autowired
    private JwtUtil jwtUtil;


    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(
            @Valid @RequestBody DangKyKhachHangRequest registerRequest
    ) {
        try {
            // 1. Tạo tài khoản
            TaiKhoanKhachHang newAccount =
                    taiKhoanKhachHangService.register(registerRequest);

            // 2. Sinh JWT ngay sau khi đăng ký
            String token = jwtUtil.generateJwtToken(
                    newAccount.getTenDangNhap()
            );

            // 3. Response
            TaiKhoanKhachHangReponse response =
                    new TaiKhoanKhachHangReponse(
                            newAccount.getMaKh(),
                            newAccount.getTenDangNhap(),
                            token,
                            "Đăng ký thành công"

                    );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(
            @Valid @RequestBody DangNhapKhachHangRequest login
    ) {
        try {
            TaiKhoanKhachHang loggedInAccount =
                    taiKhoanKhachHangService.login(login);

            // 🔐 Sinh JWT
            String token = jwtUtil.generateJwtToken(
                    loggedInAccount.getTenDangNhap()
            );

            TaiKhoanKhachHangReponse response =
                    new TaiKhoanKhachHangReponse(
                            loggedInAccount.getMaKh(),
                            loggedInAccount.getTenDangNhap(),

                            token,
                            "Đăng nhập thành công"

                    );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi hệ thống: " + e.getMessage());
        }
    }

}
