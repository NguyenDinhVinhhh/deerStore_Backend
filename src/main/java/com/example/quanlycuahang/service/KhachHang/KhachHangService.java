package com.example.quanlycuahang.service.KhachHang;

import com.example.quanlycuahang.dto.KhachHang.KhachHangResponse;
import com.example.quanlycuahang.entity.KhachHang.KhachHang;
import com.example.quanlycuahang.entity.KhachHang.NhomKhachHang;
import com.example.quanlycuahang.repository.HoaDon.HoaDonRepository;
import com.example.quanlycuahang.repository.KhachHang.KhachHangRepository;
import com.example.quanlycuahang.repository.KhachHang.NhomKhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private NhomKhachHangRepository nhomKhachHangRepository;

    // Inject HoaDonRepository
    @Autowired
    private HoaDonRepository hoaDonRepository;


    private static final int Khach_hang_moi = 16;

    //Hàm lấy khách hàng bằng id
    public KhachHang getKhachHangEntityById(Integer maKh) {
        return khachHangRepository.findById(maKh)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tìm thấy với mã: " + maKh));
    }

    //hàm lấy tổng số đơn hàng đã mua của khách hàng theo id
    @Transactional(readOnly = true)
    public Long getTongSoDonHangByMaKh(Integer maKh) {
        if (maKh == null) {
            return 0L;
        }
        return hoaDonRepository.countAllByMaKh(maKh);
    }

    //hàm chuyển từ entity khách hàng sang reponse
    private KhachHangResponse convertToDto(KhachHang khachHang) {
        KhachHangResponse dto = new KhachHangResponse(khachHang);

        if (khachHang.getMaKh() != null) {
            Long soDonHang = getTongSoDonHangByMaKh(khachHang.getMaKh());
            dto.setTongSoDonHang(soDonHang);
        } else {
            dto.setTongSoDonHang(0L);
        }

        return dto;
    }

    //Hàm tìm kiếm khách hàng theo sdt hoặc tên
    @Transactional(readOnly = true)
    public List<KhachHangResponse> searchCustomers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }
        List<KhachHang> khachHangList = khachHangRepository
                .findBySdtContainingOrHoTenContainingIgnoreCase(keyword, keyword);
        return khachHangList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    //Hàm lấy danh sách tất cả khách hàng
    @Transactional(readOnly = true)
    public List<KhachHangResponse> getAllKhachHang() {
        List<KhachHang> khachHangList = khachHangRepository.findAll();
        return khachHangList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Transactional
    public KhachHang createCustomer(KhachHang khachHang) {

        if (khachHang.getHoTen() == null || khachHang.getHoTen().isBlank()) {
            throw new RuntimeException("Họ tên không được để trống");
        }

        if (khachHang.getSdt() != null &&
                khachHangRepository.findBySdt(khachHang.getSdt()).isPresent()) {
            throw new RuntimeException("Số điện thoại đã tồn tại");
        }

        nhomKhachHangRepository.findById(Khach_hang_moi)
                .ifPresent(khachHang::setNhomKhachHang);

        khachHang.setNgayDangKy(LocalDate.now());
        khachHang.setTongChiTieuLuyKe(
                khachHang.getTongChiTieuLuyKe() == null
                        ? BigDecimal.ZERO
                        : khachHang.getTongChiTieuLuyKe()
        );

        return khachHangRepository.save(khachHang);
    }


    //Hàm này lấy phần trăm chiết khấu của khách hàng theo id
    @Transactional
    public BigDecimal getCustomerDiscountPercent(Integer maKh) {
        KhachHang kh = khachHangRepository.findById(maKh).orElse(null);
        if (kh == null) {
            return BigDecimal.ZERO;
        }

        NhomKhachHang nhom = kh.getNhomKhachHang();
        if (nhom == null || nhom.getPhanTramChietKhau() == null) {
            return BigDecimal.ZERO;
        }

        return nhom.getPhanTramChietKhau();
    }


    // Hàm set điều kiện để tăng hạng dùng sau khi thanh toán thành công
    @Transactional
    public KhachHang checkAndUpgradeRank(KhachHang khachHang) {

        BigDecimal tongChiTieu = khachHang.getTongChiTieuLuyKe();

        List<NhomKhachHang> tatCaNhom = nhomKhachHangRepository.findAll();

        tatCaNhom.sort(
                (a, b) -> b.getNguongChiTieuToiThieu().compareTo(a.getNguongChiTieuToiThieu())
        );

        NhomKhachHang nhomMoi = null;
        for (NhomKhachHang nhom : tatCaNhom) {
            if (tongChiTieu.compareTo(nhom.getNguongChiTieuToiThieu()) >= 0) {
                nhomMoi = nhom;
                break;   // tìm được nhóm phù hợp đầu tiên → thoát
            }
        }

        if (nhomMoi == null) {
            return khachHang;
        }

        NhomKhachHang nhomHienTai = khachHang.getNhomKhachHang();

        boolean laNhomMoi =
                nhomHienTai == null ||
                        !nhomMoi.getMaNhom().equals(nhomHienTai.getMaNhom());

        if (laNhomMoi) {
            khachHang.setNhomKhachHang(nhomMoi);
            System.out.println(
                    "DEBUG: Khách hàng " + khachHang.getMaKh()
                            + " được thăng hạng lên: " + nhomMoi.getTenNhom()
            );
            return khachHangRepository.save(khachHang);
        }
        return khachHang;
    }

    //hàm cập nhật chi tiêu lũy kế
    public KhachHang updateCumulativeSpending(Integer maKh, BigDecimal amountToAdd) {
        if (maKh == null || amountToAdd == null || amountToAdd.compareTo(BigDecimal.ZERO) <= 0) {
            // Không làm gì nếu không có MaKh hoặc số tiền không hợp lệ
            return null;
        }

        // Tải Khách hàng (Sử dụng KhachHangRepository.findById)
        Optional<KhachHang> optionalKhachHang = khachHangRepository.findById(maKh);

        if (optionalKhachHang.isPresent()) {
            KhachHang kh = optionalKhachHang.get();

            // Cập nhật tổng chi tiêu
            BigDecimal currentTotal = kh.getTongChiTieuLuyKe() != null ? kh.getTongChiTieuLuyKe() : BigDecimal.ZERO;
            BigDecimal newTotal = currentTotal.add(amountToAdd);

            kh.setTongChiTieuLuyKe(newTotal);

            // Lưu và trả về
            return khachHangRepository.save(kh); // Việc save này nằm trong @Transactional của InvoiceService
        }
        // Trả về null hoặc ném lỗi nếu khách hàng không tồn tại
        return null;
    }

    // Trong KhachHangService.java, thêm hàm mới:

    /**
     * Lấy giới hạn số tiền giảm tối đa (Max Cap) cho chiết khấu thành viên của khách hàng.
     * Giá trị này được lưu trong bảng NhomKhachHang.
     *
     * @param maKh Mã khách hàng.
     * @return Giới hạn tiền giảm tối đa (BigDecimal). Trả về 0 nếu không có giới hạn hoặc không tìm thấy.
     */
    @Transactional(readOnly = true) // Chỉ đọc
    public BigDecimal getMemberDiscountMaxCap(Integer maKh) {
        // 1. Tìm Khách hàng
        KhachHang kh = khachHangRepository.findById(maKh).orElse(null);
        if (kh == null) {
            // Trả về 0 nếu không tìm thấy khách hàng (an toàn)
            return BigDecimal.ZERO;
        }

        // 2. Lấy thông tin Nhóm (Hạng)
        NhomKhachHang nhom = kh.getNhomKhachHang();
        if (nhom == null) {
            // Trả về 0 nếu khách hàng chưa có nhóm
            return BigDecimal.ZERO;
        }

        // 3. Lấy giới hạn tiền giảm từ Nhóm
        if (nhom.getGioiHanTienGiamToiDa() == null) {
            // Trả về 0 nếu trường này chưa được cấu hình
            return BigDecimal.ZERO;
        }

        // Trả về giá trị Max Cap
        return nhom.getGioiHanTienGiamToiDa();
    }

    @Transactional(readOnly = true)
    public Optional<KhachHang> findBySdt(String sdt) {
        return khachHangRepository.findBySdt(sdt);
    }

    @Transactional
    public KhachHang updateCustomer(Integer maKh, KhachHang updatedData) {

        // 1. Lấy khách hàng hiện tại
        KhachHang khachHang = khachHangRepository.findById(maKh)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng với mã: " + maKh));

        // 2. Cập nhật họ tên
        if (updatedData.getHoTen() != null && !updatedData.getHoTen().isBlank()) {
            khachHang.setHoTen(updatedData.getHoTen());
        }

        // 3. Cập nhật SĐT (phải kiểm tra trùng)
        if (updatedData.getSdt() != null &&
                !updatedData.getSdt().equals(khachHang.getSdt())) {

            if (khachHangRepository.findBySdt(updatedData.getSdt()).isPresent()) {
                throw new RuntimeException("Số điện thoại đã tồn tại");
            }
            khachHang.setSdt(updatedData.getSdt());
        }

        // 4. Cập nhật email
        if (updatedData.getEmail() != null) {
            khachHang.setEmail(updatedData.getEmail());
        }

        // 5. Cập nhật địa chỉ
        if (updatedData.getDiaChi() != null) {
            khachHang.setDiaChi(updatedData.getDiaChi());
        }

        // 6. Cập nhật ghi chú
        if (updatedData.getGhiChu() != null) {
            khachHang.setGhiChu(updatedData.getGhiChu());
        }

        // ❌ Không cho phép cập nhật:
        // - ngayDangKy
        // - tongChiTieuLuyKe
        // - nhomKhachHang

        return khachHangRepository.save(khachHang);
    }


}