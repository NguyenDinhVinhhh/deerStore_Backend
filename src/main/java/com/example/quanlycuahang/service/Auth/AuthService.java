package com.example.quanlycuahang.service.Auth;


import com.example.quanlycuahang.dto.Auth.StaffRegisterRequest;
import com.example.quanlycuahang.dto.Auth.TaiKhoanDto;
import com.example.quanlycuahang.dto.Auth.UpdateTaiKhoanRequest;
import com.example.quanlycuahang.dto.ChiNhanh.ChiNhanhDto;
import com.example.quanlycuahang.entity.ChiNhanh.ChiNhanh;
import com.example.quanlycuahang.entity.ChiNhanh.TaiKhoanChiNhanh;
import com.example.quanlycuahang.entity.TaiKhoan.TaiKhoan;
import com.example.quanlycuahang.entity.VaiTro.VaiTro;
import com.example.quanlycuahang.repository.ChiNhanh.ChiNhanhRepository;
import com.example.quanlycuahang.repository.ChiNhanh.TaiKhoanChiNhanhRepository;
import com.example.quanlycuahang.repository.TaiKhoan.TaiKhoanRepository;
import com.example.quanlycuahang.repository.VaiTro.VaiTroRepository;
import com.example.quanlycuahang.service.ChiNhanh.BranchService;
import com.example.quanlycuahang.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @Autowired
    private ChiNhanhRepository chiNhanhRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    private TaiKhoanChiNhanhRepository taiKhoanChiNhanhRepository;

    @Autowired
    private BranchService branchService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TaiKhoanChiNhanhRepository chiNhanhTaiKhoanRepository;

    @Autowired
    private JwtUtil jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    public String register(TaiKhoan taiKhoan) {
        if(taiKhoanRepository.existsByTenDangNhap(taiKhoan.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        taiKhoan.setMatKhau(passwordEncoder.encode(taiKhoan.getMatKhau()));
        taiKhoanRepository.save(taiKhoan);
        return "Đăng ký thành công!";
    }

    public String login(String tenDangNhap, String matKhau) {
        // 1. Thực hiện xác thực bằng Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(tenDangNhap, matKhau)
        );

        // 2. Lưu vào Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Truyền đối tượng AUTHENTICATION vào để JwtUtil lấy được quyền (authorities)
        return jwtUtils.generateJwtToken(authentication);
    }
    public TaiKhoan registerStaff(StaffRegisterRequest request) {


        if (taiKhoanRepository.existsByTenDangNhap(request.getTenDangNhap())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }

        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setTenDangNhap(request.getTenDangNhap());
        taiKhoan.setHoTen(request.getHoTen());
        taiKhoan.setEmail(request.getEmail());
        taiKhoan.setSdt(request.getSdt());
        taiKhoan.setMaVaiTro(request.getMaVaiTro());
        taiKhoan.setTrangThai(true);
        taiKhoan.setIsSuperAdmin(false);

        taiKhoan.setMatKhau(passwordEncoder.encode(request.getMatKhau()));

        TaiKhoan savedTaiKhoan = taiKhoanRepository.save(taiKhoan);


        if (request.getDanhSachChiNhanh() != null && !request.getDanhSachChiNhanh().isEmpty()) {
            request.getDanhSachChiNhanh().forEach(maChiNhanh -> {
                TaiKhoanChiNhanh tkcn = new TaiKhoanChiNhanh();
                tkcn.setMaTk(savedTaiKhoan.getMaTk());
                tkcn.setMaChiNhanh(maChiNhanh);
                taiKhoanChiNhanhRepository.save(tkcn);
            });
        } else {
            throw new RuntimeException("Tài khoản phải được phân công ít nhất một chi nhánh!");
        }

        return savedTaiKhoan;
    }


    // hàm lấy tất cả tài khoản
    public List<TaiKhoan> getAllTaiKhoan() {
        return taiKhoanRepository.findAll();
    }

    public TaiKhoan updateTaiKhoan(Integer maTk, UpdateTaiKhoanRequest request) {
        TaiKhoan existingTk = taiKhoanRepository.findById(maTk)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));

        existingTk.setHoTen(request.getHoTen());
        existingTk.setEmail(request.getEmail());
        existingTk.setSdt(request.getSdt());
        existingTk.setMaVaiTro(request.getMaVaiTro());
        existingTk.setTrangThai(request.getTrangThai());

        if(request.getMatKhau() != null && !request.getMatKhau().isEmpty()) {
            existingTk.setMatKhau(passwordEncoder.encode(request.getMatKhau()));
        }
        if(request.getDanhSachChiNhanh() != null && !request.getDanhSachChiNhanh().isEmpty()) {
            List<ChiNhanh> newBranches = chiNhanhRepository.findAllById(request.getDanhSachChiNhanh());
            existingTk.setChiNhanhList(newBranches);
        }
        return taiKhoanRepository.save(existingTk);
    }


    public List<TaiKhoanDto> getAllTaiKhoanDto() {
        return taiKhoanRepository.findAll()
                .stream()
                .map(tk -> {
                    // Lấy danh sách chi nhánh của tài khoản
                    List<ChiNhanhDto> branches = branchService.getBranchesByMaTk(tk.getMaTk());
                    Optional<VaiTro> vaiTroOpt = vaiTroRepository.findById(tk.getMaVaiTro());
                    String tenVaiTro = vaiTroOpt
                            .map(VaiTro::getTenVaiTro)
                            .orElse("Không xác định");
                    return new TaiKhoanDto(
                            tk.getMaTk(),
                            tk.getTenDangNhap(),
                            tk.getHoTen(),
                            tk.getEmail(),
                            tk.getSdt(),
                            tk.getTrangThai(),
                            tk.getMaVaiTro(),
                            tenVaiTro,
                            tk.getIsSuperAdmin(),
                            branches
                    );
                })
                .collect(Collectors.toList());
    }

    //tìm kiếm danh sách tài khoàn bằng tên
    public List<TaiKhoanDto> searchTaiKhoanByHoTen(String hoTen) {
        return taiKhoanRepository.findByHoTenContainingIgnoreCase(hoTen)
                .stream()
                .map(tk -> {

                    List<ChiNhanhDto> branches = branchService.getBranchesByMaTk(tk.getMaTk());
                    Optional<VaiTro> vaiTroOpt = vaiTroRepository.findById(tk.getMaVaiTro());
                    String tenVaiTro = vaiTroOpt
                            .map(VaiTro::getTenVaiTro)
                            .orElse("Không xác định");

                    return new TaiKhoanDto(
                            tk.getMaTk(),
                            tk.getTenDangNhap(),
                            tk.getHoTen(),
                            tk.getEmail(),
                            tk.getSdt(),
                            tk.getTrangThai(),
                            tk.getMaVaiTro(),
                            tenVaiTro,
                            tk.getIsSuperAdmin(),
                            branches
                    );
                })
                .collect(Collectors.toList());
    }


}