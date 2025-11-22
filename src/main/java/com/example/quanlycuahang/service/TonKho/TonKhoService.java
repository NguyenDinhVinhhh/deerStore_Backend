package com.example.quanlycuahang.service.TonKho;

import com.example.quanlycuahang.dto.HoaDon.InvoiceItemDto;
import com.example.quanlycuahang.dto.TonKho.BaoCaoTonKhoDTO;
import com.example.quanlycuahang.dto.TonKho.TonKhoAdjustmentRequest;
import com.example.quanlycuahang.dto.TonKho.TonKhoRequest;
import com.example.quanlycuahang.entity.TonKho.TonKho;
import com.example.quanlycuahang.entity.TonKho.TonKhoId;
import com.example.quanlycuahang.exception.InventoryAdjustmentException;
import com.example.quanlycuahang.exception.ResourceNotFoundException;
import com.example.quanlycuahang.repository.KhoHang.KhoHangRepository;
import com.example.quanlycuahang.repository.SanPham.SanPhamRepository;
import com.example.quanlycuahang.repository.TonKho.TonKhoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TonKhoService {

    private final TonKhoRepository tonKhoRepository;
    private final SanPhamRepository sanPhamRepository;
    private final KhoHangRepository khoHangRepository;

    // Constructor Injection
    public TonKhoService(TonKhoRepository tonKhoRepository, SanPhamRepository sanPhamRepository, KhoHangRepository khoHangRepository) {
        this.tonKhoRepository = tonKhoRepository;
        this.sanPhamRepository = sanPhamRepository;
        this.khoHangRepository = khoHangRepository;
    }

    // --- LOGIC CẬP NHẬT TỒN KHO ---

    /**
     * Cập nhật hoặc thêm mới tồn kho.
     * Đây là thao tác Cập nhật Tuyệt đối (Set), không phải Tăng/Giảm (Adjust).
     */
    @Transactional
    public TonKho updateInventory(TonKhoRequest request) {
        if (!sanPhamRepository.existsById(request.getMaSp())) {
            // Thay thế RuntimeException
            throw new ResourceNotFoundException("Sản phẩm với mã " + request.getMaSp() + " không tồn tại.");
        }
        if (!khoHangRepository.existsById(request.getMaKho())) {
            // Thay thế RuntimeException
            throw new ResourceNotFoundException("Kho hàng với mã " + request.getMaKho() + " không tồn tại.");
        }

        // 2. Tạo ID phức hợp
        TonKhoId id = new TonKhoId();
        id.setMaSp(request.getMaSp());
        id.setMaKho(request.getMaKho());

        // 3. Tìm bản ghi tồn kho hiện tại (nếu có)
        Optional<TonKho> existingTonKho = tonKhoRepository.findById(id);
        TonKho tonKho;

        if (existingTonKho.isPresent()) {
            // Cập nhật bản ghi đã tồn tại
            tonKho = existingTonKho.get();
        } else {
            // Tạo bản ghi mới
            tonKho = new TonKho();
            tonKho.setId(id);
            // Thiết lập mối quan hệ ManyToOne (cần thiết nếu bạn muốn truy cập .getSanPham() hoặc .getKhoHang())
            // Giả sử SanPhamRepository và KhoHangRepository có phương thức findById
            tonKho.setSanPham(sanPhamRepository.findById(request.getMaSp()).get());
            tonKho.setKhoHang(khoHangRepository.findById(request.getMaKho()).get());
        }

        // 4. Thiết lập giá trị mới
        tonKho.setSoLuongTon(request.getSoLuongTon());
        tonKho.setGhiChu(request.getGhiChu());
        tonKho.setNgayCapNhat(LocalDateTime.now());

        // 5. Lưu vào DB
        return tonKhoRepository.save(tonKho);
    }

    // --- LOGIC TRUY VẤN TỒN KHO ---

    /**
     * Lấy chi tiết tồn kho theo Mã sản phẩm (trên tất cả các kho).
     */
    public List<TonKho> getInventoryDetailsByProduct(Integer maSp) {
        return tonKhoRepository.findByIdMaSp(maSp);
    }

    /**
     * Tính tổng tồn kho của một sản phẩm trên tất cả các kho.
     */
    public Integer getTotalStockByProduct(Integer maSp) {
        Integer total = tonKhoRepository.calculateTotalStockByMaSp(maSp);
        return total != null ? total : 0;
    }

    /**
     * Lấy danh sách tồn kho theo Mã kho.
     */
    public List<TonKho> getInventoryByWarehouse(Integer maKho) {
        return tonKhoRepository.findByIdMaKho(maKho);
    }

    // File: com.yourpackage.service.TonKhoService.java (Thêm vào class TonKhoService)

// ... Các import và constructor đã có ...

    /**
     * Điều chỉnh (Tăng/Giảm) số lượng tồn kho.
     * @param request Chứa maSp, maKho và soLuong (có thể âm hoặc dương).
     * @return Bản ghi TonKho đã được cập nhật.
     */

    @Transactional
    public TonKho adjustInventory(TonKhoAdjustmentRequest request) {
        // 1. Kiểm tra tính hợp lệ của Mã SP và Mã Kho
        if (!sanPhamRepository.existsById(request.getMaSp())) {
            throw new ResourceNotFoundException("Sản phẩm với mã " + request.getMaSp() + " không tồn tại.");
        }
        if (!khoHangRepository.existsById(request.getMaKho())) {
            throw new ResourceNotFoundException("Kho hàng với mã " + request.getMaKho() + " không tồn tại.");
        }

        // 2. Tạo ID phức hợp
        TonKhoId id = new TonKhoId();
        id.setMaSp(request.getMaSp());
        id.setMaKho(request.getMaKho());

        // 3. Tìm bản ghi tồn kho hiện tại
        TonKho tonKho = tonKhoRepository.findById(id).orElseGet(() -> {
            // Nếu chưa có bản ghi, tạo mới với tồn kho ban đầu là 0
            TonKho newTonKho = new TonKho();
            newTonKho.setId(id);
            newTonKho.setSoLuongTon(0);

            // Đây là nơi bạn cần đảm bảo các Repository trả về Optional.get() an toàn
            // (Chúng ta đang giả định ID có tồn tại do đã kiểm tra ở bước 1)
            newTonKho.setSanPham(sanPhamRepository.findById(request.getMaSp()).get());
            newTonKho.setKhoHang(khoHangRepository.findById(request.getMaKho()).get());
            return newTonKho;
        });

        // 4. Tính toán số lượng tồn kho mới (Phải làm trước khi kiểm tra)
        int currentStock = tonKho.getSoLuongTon();
        int adjustment = request.getSoLuong();
        int newStock = currentStock + adjustment;

        // Logic nghiệp vụ quan trọng: Ngăn chặn tồn kho âm (Đã di chuyển xuống đây)
        if (newStock < 0) {
            throw new InventoryAdjustmentException("Không thể điều chỉnh do tồn kho không đủ (Hiện tại: " + currentStock + ", Điều chỉnh: " + adjustment + ")");
        }

        // 5. Cập nhật và lưu
        tonKho.setSoLuongTon(newStock);
        tonKho.setGhiChu(request.getGhiChu());
        tonKho.setNgayCapNhat(LocalDateTime.now());

        return tonKhoRepository.save(tonKho);
    }

    /**
     * Lấy tổng tồn kho theo danh mục.
     * Cần DanhMucRepository để lấy tên danh mục (tạm bỏ qua cho đơn giản).
     */
    public List<BaoCaoTonKhoDTO> getTotalStockByCategory() {
        List<Object[]> results = tonKhoRepository.findTotalStockByDanhMuc();

        return results.stream()
                .map(result -> {
                    Integer maDanhMuc = (Integer) result[0];
                    Long tongTon = ((Number) result[1]).longValue();

                    // TODO: Thực hiện tra cứu tên danh mục tại đây (cần DanhMucRepository)
                    String tenDanhMuc = "DM_" + maDanhMuc;

                    return new BaoCaoTonKhoDTO(maDanhMuc, tenDanhMuc, tongTon);
                })
                .collect(Collectors.toList());
    }

    /**
     * Lấy Top N sản phẩm có tồn kho thấp nhất.
     */
    public List<BaoCaoTonKhoDTO> getTopLowStockProducts(Integer limit) {
        List<Object[]> results = tonKhoRepository.findTopLowStockProducts(limit);

        return results.stream()
                .map(result -> {
                    Integer maSp = (Integer) result[0];
                    Long tongTon = ((Number) result[1]).longValue();

                    // TODO: Thực hiện tra cứu tên sản phẩm tại đây (cần SanPhamRepository)
                    String tenSp = "SP_" + maSp;

                    return new BaoCaoTonKhoDTO(maSp, tenSp, tongTon);
                })
                .collect(Collectors.toList());
    }

    /**
     * Lấy Top N sản phẩm có tồn kho thấp nhất tại một kho cụ thể.
     */
    public List<BaoCaoTonKhoDTO> getTopLowStockProductsByWarehouse(Integer maKho, Integer limit) {

        // 1. Kiểm tra tồn tại Kho hàng (tùy chọn, nhưng nên có)
        if (!khoHangRepository.existsById(maKho)) {
            throw new ResourceNotFoundException("Kho hàng với mã " + maKho + " không tồn tại.");
        }

        // 2. Thực hiện truy vấn
        List<Object[]> results = tonKhoRepository.findTopLowStockProductsByWarehouse(maKho, limit);

        // 3. Mapping kết quả sang DTO
        return results.stream()
                .map(result -> {
                    Integer maSp = (Integer) result[0];
                    // Chú ý: so_luong_ton là Integer, cần cast an toàn
                    Long soLuongTon = ((Number) result[1]).longValue();

                    // TODO: Lấy tên sản phẩm từ SanPhamRepository (nếu cần hiển thị tên)
                    String tenSp = "SP_" + maSp;

                    return new BaoCaoTonKhoDTO(maSp, tenSp, soLuongTon);
                })
                .collect(Collectors.toList());
    }


    // File: com.example.quanlycuahang.service.TonKho.TonKhoService.java

// ... (Các phương thức khác đã có)

    /**
     * Tìm kiếm sản phẩm còn tồn kho theo từ khóa (Tên SP/SKU) tại một Kho hàng cụ thể.
     * @param maKho Mã kho hàng của chi nhánh hiện tại.
     * @param query Từ khóa tìm kiếm.
     * @param limit Số lượng kết quả tối đa (dùng để cắt bớt kết quả ở tầng Service/Controller).
     * @return Danh sách TonKho khớp với tìm kiếm, chứa cả thông tin SanPham và soLuongTon.
     */
    @Transactional(readOnly = true) // Đây là thao tác chỉ đọc
    public List<TonKho> searchAvailableProducts(Integer maKho, String query, Integer limit) {
        // 1. Kiểm tra tồn tại Kho hàng (Tùy chọn, nhưng nên có)
        if (!khoHangRepository.existsById(maKho)) {
            throw new ResourceNotFoundException("Kho hàng với mã " + maKho + " không tồn tại.");
        }

        // 2. Gọi hàm tìm kiếm từ Repository
        // Thêm kiểm tra query null/empty (nếu query rỗng, ta có thể không tìm kiếm hoặc trả về top bán chạy)
        if (query == null || query.trim().isEmpty()) {
            // Tạm thời trả về danh sách rỗng nếu query rỗng để tránh trả về toàn bộ DB
            return List.of();
        }

        List<TonKho> results = tonKhoRepository.searchProductsByQueryAndWarehouse(maKho, query);

        // 3. Xử lý giới hạn kết quả (LIMIT) ở tầng Service (vì JPQL không hỗ trợ LIMIT dễ dàng)
        if (limit != null && results.size() > limit) {
            return results.subList(0, limit);
        }

        return results;
    }

    public void deductStock(Integer maKho, List<InvoiceItemDto> items) {
        for (InvoiceItemDto item : items) {

            // TRỪ TỒN KHO (Sử dụng Repository)
            int rowsAffected = tonKhoRepository.updateStockQuantitySubtract(item.getMaSp(), maKho, item.getSoLuong());

            // XÁC THỰC LỖI TỒN KHO
            if (rowsAffected == 0) {
                // rowsAffected = 0 nghĩa là không tìm thấy bản ghi tồn kho tương ứng để trừ
                throw new IllegalStateException("Lỗi tồn kho: Không tìm thấy tồn kho cho SP " + item.getMaSp() + " tại Kho " + maKho + ".");
            }

            // Xác thực tồn kho âm (Đảm bảo việc trừ không dẫn đến số lượng tồn kho < 0)
            // Lưu ý: Tùy thuộc vào cách bạn triển khai hàm updateStockQuantitySubtract,
            // nếu nó đã bao gồm kiểm tra âm thì bước này có thể không cần thiết,
            // nhưng việc kiểm tra lại là an toàn.
            if (tonKhoRepository.findByMaSpAndMaKho(item.getMaSp(), maKho).get().getSoLuongTon() < 0) {
                throw new IllegalStateException("Lỗi tồn kho: Số lượng tồn kho của SP " + item.getMaSp() + " tại Kho " + maKho + " bị âm sau khi trừ.");
            }
        }
    }
}