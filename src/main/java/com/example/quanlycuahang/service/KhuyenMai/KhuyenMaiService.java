package com.example.quanlycuahang.service.KhuyenMai;

import com.example.quanlycuahang.entity.KhuyenMai.KhuyenMai;
import com.example.quanlycuahang.repository.KhuyenMai.KhuyenMaiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class KhuyenMaiService {

    @Autowired
    private KhuyenMaiRepository khuyenMaiRepository;

    //hàm thêm mới khuyến mãi
    @Transactional
    public KhuyenMai createKhuyenMai(KhuyenMai khuyenMai) {
        if (khuyenMai.getMaCode() != null && khuyenMaiRepository.findByMaCode(khuyenMai.getMaCode()).isPresent()) {
            throw new RuntimeException("Mã Code khuyến mãi đã tồn tại.");
        }
        if (khuyenMai.getTrangThai() == null) {
            khuyenMai.setTrangThai(1);
        }
        // 💡 LƯU Ý: Khi thêm mới, cần đảm bảo các trường mới (giaTriDonHangToiThieu, gioiHanTienGiamToiDa)
        // được set giá trị hợp lệ (thường là 0 nếu không áp dụng).
        return khuyenMaiRepository.save(khuyenMai);
    }

    // hàm cập nhật khuyến mãi
    @Transactional
    public KhuyenMai updateKhuyenMai(Integer maKm, KhuyenMai updatedKhuyenMai) {
        return khuyenMaiRepository.findById(maKm)
                .map(km -> {

                    Optional.ofNullable(updatedKhuyenMai.getTenKm()).ifPresent(km::setTenKm);
                    Optional.ofNullable(updatedKhuyenMai.getMoTa()).ifPresent(km::setMoTa);
                    Optional.ofNullable(updatedKhuyenMai.getLoaiKm()).ifPresent(km::setLoaiKm);
                    Optional.ofNullable(updatedKhuyenMai.getGiaTri()).ifPresent(km::setGiaTri);
                    Optional.ofNullable(updatedKhuyenMai.getDieuKienApDung()).ifPresent(km::setDieuKienApDung);
                    Optional.ofNullable(updatedKhuyenMai.getNgayBatDau()).ifPresent(km::setNgayBatDau);
                    Optional.ofNullable(updatedKhuyenMai.getNgayKetThuc()).ifPresent(km::setNgayKetThuc);
                    Optional.ofNullable(updatedKhuyenMai.getTrangThai()).ifPresent(km::setTrangThai);

                    // 💡 BỔ SUNG: Cập nhật các trường giới hạn mới
                    Optional.ofNullable(updatedKhuyenMai.getGiaTriDonHangToiThieu()).ifPresent(km::setGiaTriDonHangToiThieu);
                    Optional.ofNullable(updatedKhuyenMai.getGioiHanTienGiamToiDa()).ifPresent(km::setGioiHanTienGiamToiDa);

                    // 🛑 LƯU Ý: Trường MaCode nên được xử lý cẩn thận nếu muốn thay đổi, nhưng giữ nguyên ở đây.

                    return khuyenMaiRepository.save(km);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Khuyến mãi với mã: " + maKm));
    }

    // hàm xóa khuyến mãi
    @Transactional
    public void deleteKhuyenMai(Integer maKm) {
        if (!khuyenMaiRepository.existsById(maKm)) {
            throw new RuntimeException("Không tìm thấy Khuyến mãi để xóa.");
        }
        khuyenMaiRepository.deleteById(maKm);
    }

    // hàm lấy danh sách khuyến mãi
    public List<KhuyenMai> getAllKhuyenMai() {
        return khuyenMaiRepository.findAll();
    }

    /**
     * Hàm kiểm tra và lấy khuyến mãi hợp lệ theo mã code (sẵn sàng cho tính toán).
     * (Sử dụng tên mới rõ ràng hơn)
     */
    @Transactional(readOnly = true)
    public Optional<KhuyenMai> getValidKhuyenMaiByCodeForCalculation(String maCode) {
        if (maCode == null) return Optional.empty();

        LocalDate today = LocalDate.now();

        // 💡 LƯU Ý: Tôi đã đổi tên hàm hiện có của bạn để phù hợp với quy ước
        // và mục đích sử dụng trong InvoiceCalculationService.
        return khuyenMaiRepository.findByMaCode(maCode)
                .filter(km -> km.getTrangThai() != null && km.getTrangThai() == 1) // Kiểm tra Trạng thái
                .filter(km -> !km.getNgayBatDau().isAfter(today)) // Ngày bắt đầu đã qua
                .filter(km -> !km.getNgayKetThuc().isBefore(today)); // Ngày kết thúc chưa tới

    }
}