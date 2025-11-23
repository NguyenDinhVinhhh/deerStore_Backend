package com.example.quanlycuahang.service.KhachHang;

import com.example.quanlycuahang.dto.KhachHang.DangKyKhachHangRequest;
import com.example.quanlycuahang.dto.KhachHang.DangNhapKhachHangRequest;
import com.example.quanlycuahang.entity.KhachHang.KhachHang;
import com.example.quanlycuahang.entity.TaiKhoanKhachHang.TaiKhoanKhachHang;

import com.example.quanlycuahang.repository.KhachHang.TaiKhoanKhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class TaiKhoanKhachHangService {

    @Autowired
    private TaiKhoanKhachHangRepository taiKhoanKhachHangRepository;

    @Autowired
    private KhachHangService khachHangService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /// Hàm đăng ký tài khoản khách hàng
    @Transactional
    public TaiKhoanKhachHang register(DangKyKhachHangRequest request) {

        if (taiKhoanKhachHangRepository.findByTenDangNhap(request.getTenDangNhap()).isPresent()) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại.");
        }

        KhachHang khachHang;
        Optional<KhachHang> existingKhachHang = khachHangService.findBySdt(request.getSdt());

        if (existingKhachHang.isPresent()) {
            khachHang = existingKhachHang.get();
            if (khachHang.getTaiKhoan() != null) {
                throw new RuntimeException("Khách hàng với SĐT " + request.getSdt() + " đã có tài khoản.");
            }
        } else {

            KhachHang newKh = new KhachHang();
            newKh.setHoTen(request.getHoTen());
            newKh.setSdt(request.getSdt());

            khachHang = khachHangService.createCustomer(newKh);
        }

        TaiKhoanKhachHang taiKhoan = new TaiKhoanKhachHang();

        taiKhoan.setKhachHang(khachHang);

        taiKhoan.setTenDangNhap(request.getTenDangNhap());

        String encodedPassword = passwordEncoder.encode(request.getMatKhau());
        taiKhoan.setMatKhau(encodedPassword);

        taiKhoan.setNgayTao(LocalDate.now());
        taiKhoan.setTrangThai((byte) 1);

        return taiKhoanKhachHangRepository.save(taiKhoan);
    }

    ///  Hàm đăng nhập tài khoản khách hàng
    @Transactional(readOnly = true)
    public TaiKhoanKhachHang login(DangNhapKhachHangRequest request) {

        TaiKhoanKhachHang taiKhoan = taiKhoanKhachHangRepository.findByTenDangNhap(request.getTenDangNhap())
                .orElseThrow(() -> new RuntimeException("Tên đăng nhập hoặc mật khẩu không đúng."));

        if (taiKhoan.getTrangThai() != 1) {
            throw new RuntimeException("Tài khoản đã bị khóa hoặc không hoạt động.");
        }

        if (!passwordEncoder.matches(request.getMatKhau(), taiKhoan.getMatKhau())) {
            throw new RuntimeException("Tên đăng nhập hoặc mật khẩu không đúng.");
        }
        return taiKhoan;
    }
}
