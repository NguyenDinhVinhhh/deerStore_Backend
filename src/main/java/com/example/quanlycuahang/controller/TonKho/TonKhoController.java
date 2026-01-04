package com.example.quanlycuahang.controller.TonKho;

import com.example.quanlycuahang.dto.TonKho.BaoCaoTonKhoDTO;
import com.example.quanlycuahang.dto.TonKho.SanPhamTonKhoResponse;
import com.example.quanlycuahang.dto.TonKho.TonKhoAdjustmentRequest;
import com.example.quanlycuahang.dto.TonKho.TonKhoRequest;
import com.example.quanlycuahang.entity.TonKho.TonKho;
import com.example.quanlycuahang.service.ChiNhanh.BranchService;
import com.example.quanlycuahang.service.TonKho.TonKhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/ton-kho")
public class TonKhoController {

    private final TonKhoService tonKhoService;
    @Autowired
    private BranchService chiNhanhService;
    public TonKhoController(TonKhoService tonKhoService) {
        this.tonKhoService = tonKhoService;
    }


    // API thiết lập tồn kho ban đầu
    @PostMapping
    public ResponseEntity<?> initInventory(
            @Valid @RequestBody TonKhoRequest request) {

        tonKhoService.updateInventory(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Thiết lập tồn kho ban đầu thành công");
    }


    //api lấy tổng số lượng tồn kho của sản phẩm trên tất cả các kho
    @GetMapping("/products/{maSp}/total")
    public ResponseEntity<Integer> getTotalStockByProduct(@PathVariable Integer maSp) {
        Integer totalStock = tonKhoService.getTotalStockByProduct(maSp);
        return ResponseEntity.ok(totalStock);
    }


    //api lấy chi tiết tồn kho của sản phẩm tại từng kho
    @GetMapping("/products/{maSp}/details")
    public ResponseEntity<List<TonKho>> getInventoryDetailsByProduct(@PathVariable Integer maSp) {
        List<TonKho> details = tonKhoService.getInventoryDetailsByProduct(maSp);
        return ResponseEntity.ok(details);
    }

    // api lấy danh sách tồn kho của tất cả sản phẩm trong 1 kho
    @GetMapping("/warehouses/{maKho}")
    public ResponseEntity<List<TonKho>> getInventoryByWarehouse(@PathVariable Integer maKho) {
        List<TonKho> inventoryList = tonKhoService.getInventoryByWarehouse(maKho);
        return ResponseEntity.ok(inventoryList);
    }


    /// api bán hàng / xuất kho hoặc nhập thêm nhập kho dựa vào số lượng âm hay dương
    @PutMapping("/adjust")
    public ResponseEntity<TonKho> adjustInventory(
            @Valid @RequestBody TonKhoAdjustmentRequest request) {
        TonKho adjustedTonKho = tonKhoService.adjustInventory(request);
        return new ResponseEntity<>(adjustedTonKho, HttpStatus.OK);
    }

    /// api báo cáo tồn kho theo danh mục sản phẩm
    @GetMapping("/report/by-category")
    public ResponseEntity<List<BaoCaoTonKhoDTO>> getReportByCategory() {
        List<BaoCaoTonKhoDTO> report = tonKhoService.getTotalStockByCategory();
        return ResponseEntity.ok(report);
    }

    // api báo cáo top 10 sản phẩm tồn kho thấp nhất trên tất cả các kho
    @GetMapping("/report/low-stock")
    public ResponseEntity<List<BaoCaoTonKhoDTO>> getTopLowStockProducts(
            @RequestParam(defaultValue = "10") Integer limit) {

        if (limit <= 0) {
            return ResponseEntity.badRequest().build();
        }
        List<BaoCaoTonKhoDTO> report = tonKhoService.getTopLowStockProducts(limit);
        return ResponseEntity.ok(report);
    }

    // --- 8. API BÁO CÁO: TOP SẢN PHẨM TỒN KHO THẤP NHẤT THEO KHO ---
    /**
     * Endpoint: GET /api/inventories/report/warehouses/{maKho}/low-stock?limit=10
     * Chức năng: Lấy Top N sản phẩm có tồn kho thấp nhất tại một kho.
     */
    @GetMapping("/report/warehouses/{maKho}/low-stock")
    public ResponseEntity<List<BaoCaoTonKhoDTO>> getTopLowStockProductsByWarehouse(
            @PathVariable Integer maKho,
            @RequestParam(defaultValue = "10") Integer limit) {

        if (limit <= 0) {
            return ResponseEntity.badRequest().build();
        }

        // GlobalExceptionHandler sẽ bắt lỗi nếu maKho không tồn tại
        List<BaoCaoTonKhoDTO> report = tonKhoService.getTopLowStockProductsByWarehouse(maKho, limit);
        return ResponseEntity.ok(report);
    }

    // File: com.yourpackage.controller.TonKhoController.java (Hoặc ChiNhanhController)

    @GetMapping("/report/branches/{maCn}/low-stock")
    public ResponseEntity<List<BaoCaoTonKhoDTO>> getTopLowStockProductsByBranch(
            @PathVariable Integer maCn,
            @RequestParam(defaultValue = "10") Integer limit) {

        // 1. Lấy mã kho từ mã chi nhánh
        Integer maKho = chiNhanhService.findMaKhoByMaChiNhanh(maCn);

        // 2. Tái sử dụng logic tồn kho
        List<BaoCaoTonKhoDTO> report = tonKhoService.getTopLowStockProductsByWarehouse(maKho, limit);

        return ResponseEntity.ok(report);
    }


    /**
     * Endpoint: GET /api/ton-kho/search
     * Chức năng: Tìm kiếm sản phẩm còn tồn kho theo Tên/SKU tại một kho cụ thể.
     * Dùng cho chức năng POS (Point of Sale)
     *
     * @param maKho Mã kho hàng của chi nhánh hiện tại (Từ Session/Auth).
     * @param query Từ khóa tìm kiếm (Tên SP, SKU).
     * @param limit Số lượng kết quả tối đa (Mặc định 10).
     * @return Danh sách TonKho (chứa SanPham và soLuongTon).
     */
    @GetMapping("/search")
    public ResponseEntity<List<TonKho>> searchProductsForPos(
            @RequestParam Integer maKho,
            @RequestParam String query,
            @RequestParam(defaultValue = "10") Integer limit) {

        // 1. Kiểm tra query rỗng (có thể chuyển logic này sang Service)
        if (query == null || query.trim().isEmpty()) {
            return ResponseEntity.ok(List.of()); // Trả về danh sách rỗng nếu không có từ khóa
        }

        // 2. Gọi Service để tìm kiếm
        List<TonKho> searchResults = tonKhoService.searchAvailableProducts(maKho, query, limit);

        // 3. Trả về kết quả
        return ResponseEntity.ok(searchResults);
    }

    @GetMapping
    public ResponseEntity<List<SanPhamTonKhoResponse>> getTonKhoTheoKho(
            @RequestParam Integer maKho
    ) {
        return ResponseEntity.ok(
                tonKhoService.getSanPhamTonKhoTheoKho(maKho)
        );
    }
}