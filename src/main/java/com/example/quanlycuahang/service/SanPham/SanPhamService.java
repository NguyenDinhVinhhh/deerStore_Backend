package com.example.quanlycuahang.service.SanPham;


import com.example.quanlycuahang.dto.SanPham.SanPhamResponse;
import com.example.quanlycuahang.entity.DanhMuc.DanhMuc;
import com.example.quanlycuahang.entity.SanPham.SanPham;
import com.example.quanlycuahang.entity.TonKho.TonKho;
import com.example.quanlycuahang.repository.DanhMuc.DanhMucRepository;
import com.example.quanlycuahang.repository.SanPham.SanPhamRepository;
import com.example.quanlycuahang.service.TonKho.TonKhoService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.Paths;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SanPhamService {

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private DanhMucRepository danhMucRepository;

    @Autowired
    private TonKhoService tonKhoService;

    @Autowired
    private FileUploadService fileUploadService;

    @Value("${app.image-base-url}")
    private String imageBaseUrl;



    // hàm tìm sản phẩm theo id
    public SanPham findById(Integer id) {
        return sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Sản phẩm có ID: " + id));
    }

    private SanPhamResponse mapToResponse(SanPham sanPham) {

        DanhMuc danhMuc = sanPham.getDanhMuc();
        Integer maSp = sanPham.getMaSp();
        Integer tonKhoDetails = tonKhoService.getTotalStockByProduct(maSp);

        return SanPhamResponse.builder()
                .maSp(sanPham.getMaSp())
                .maSku(sanPham.getMaSku())
                .tenSp(sanPham.getTenSp())
                .moTa(sanPham.getMoTa())
                .donGia(sanPham.getDonGia())
                .giaVon(sanPham.getGiaVon())
                .hinhAnhUrl(
                        sanPham.getHinhAnh() != null
                                ? imageBaseUrl + sanPham.getHinhAnh()
                                : null
                )
                .maDanhMuc(sanPham.getMaDanhMuc())
                .tenDanhMuc(
                        danhMuc != null
                                ? danhMuc.getTenDanhMuc()
                                : "Không xác định"
                )
                .trangThai(sanPham.getTrangThai())
                .tonKhoTong(tonKhoDetails)

                // ====== 3 FIELD MỚI ======
                .soManhGhep(sanPham.getSoManhGhep())
                .thoiGianGhep(sanPham.getThoiGianGhep())
                .doKho(sanPham.getDoKho())

                .build();
    }


    public SanPhamResponse findByIdResponse(Integer id) {
        SanPham sanPham = findById(id);
        return mapToResponse(sanPham);
    }

    public List<SanPhamResponse> findAll() {
        return sanPhamRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ==================== TẠO MỚI (DÙNG @RequestParam) ====================
    @Transactional
    public SanPhamResponse createByParams(
            String tenSP,
            String maSku,
            BigDecimal donGia,
            BigDecimal giaVon,
            String moTa,
            Integer maDanhMuc,
            MultipartFile hinhAnhFile
    ) {
        // Kiểm tra trùng SKU
        if (sanPhamRepository.findByMaSku(maSku).isPresent()) {
            throw new RuntimeException("Mã SKU đã tồn tại: " + maSku);
        }

        // Kiểm tra danh mục
        danhMucRepository.findById(maDanhMuc)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại: " + maDanhMuc));

        String fileName = null;
        if (hinhAnhFile != null && !hinhAnhFile.isEmpty()) {
            fileName = fileUploadService.storeFile(hinhAnhFile);
        }

        SanPham sp = new SanPham();
        sp.setTenSp(tenSP);
        sp.setMaSku(maSku);
        sp.setDonGia(donGia);
        sp.setGiaVon(giaVon);
        sp.setMoTa(moTa);
        sp.setMaDanhMuc(maDanhMuc);
        sp.setTrangThai(true);
        sp.setHinhAnh(fileName);

        SanPham saved = sanPhamRepository.save(sp);
        return findByIdResponse(saved.getMaSp());
    }

    public SanPhamResponse updateThongTinSanPham(
            Integer id,
            String tenSP,
            String maSku,
            String moTa,
            Integer maDanhMuc,
            Integer soManhGhep,
            Integer thoiGianGhep,
            Integer doKho,
            MultipartFile hinhAnhFile
    ) {
        // 1️⃣ Lấy sản phẩm
        SanPham sp = findById(id);

        // 2️⃣ Kiểm tra SKU trùng (nếu có thay đổi)
        sanPhamRepository.findByMaSku(maSku)
                .ifPresent(existing -> {
                    if (!existing.getMaSp().equals(id)) {
                        throw new RuntimeException("Mã SKU đã tồn tại ở sản phẩm khác.");
                    }
                });

        // 3️⃣ Kiểm tra danh mục tồn tại
        danhMucRepository.findById(maDanhMuc)
                .orElseThrow(() ->
                        new RuntimeException("Danh mục không tồn tại: " + maDanhMuc)
                );

        // 4️⃣ Xử lý hình ảnh
        String oldFile = sp.getHinhAnh();
        String newFile = oldFile;

        if (hinhAnhFile != null && !hinhAnhFile.isEmpty()) {
            // Xóa ảnh cũ nếu có
            if (oldFile != null && !oldFile.isBlank()) {
                fileUploadService.deleteFile(oldFile);
            }
            // Lưu ảnh mới
            newFile = fileUploadService.storeFile(hinhAnhFile);
        }

        // 5️⃣ Cập nhật thông tin (KHÔNG cập nhật giá)
        sp.setTenSp(tenSP);
        sp.setMaSku(maSku);
        sp.setMoTa(moTa);
        sp.setMaDanhMuc(maDanhMuc);
        sp.setSoManhGhep(soManhGhep);
        sp.setThoiGianGhep(thoiGianGhep);
        sp.setDoKho(doKho);
        sp.setHinhAnh(newFile);

        // 6️⃣ Lưu DB
        SanPham updated = sanPhamRepository.save(sp);

        // 7️⃣ Trả response
        return findByIdResponse(updated.getMaSp());
    }


    public SanPhamResponse updateGiaSanPham(
            Integer id,
            BigDecimal donGia,
            BigDecimal giaVon
    ) {
        SanPham sp = findById(id);

        if (giaVon.compareTo(donGia) > 0) {
            throw new RuntimeException("Giá vốn không được lớn hơn giá bán");
        }

        sp.setDonGia(donGia);
        sp.setGiaVon(giaVon);

        return findByIdResponse(sanPhamRepository.save(sp).getMaSp());
    }


    public void delete(Integer id) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với id: " + id));

        // Nếu có ảnh thì xóa file ảnh trong thư mục uploads (nếu bạn có lưu)
        if (sanPham.getHinhAnh() != null) {
            Path path = Paths.get("uploads", "images", sanPham.getHinhAnh());
            try {
                Files.deleteIfExists(path);
            } catch (IOException e) {
                throw new RuntimeException("Không thể xóa ảnh: " + e.getMessage());
            }
        }

        sanPhamRepository.delete(sanPham);
    }

    public SanPhamResponse findBySku(String maSku) {
        SanPham sp = sanPhamRepository.findByMaSku(maSku)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với SKU: " + maSku));

        return mapToResponse(sp);
    }

    //hàm tìm kiếm theo tên hoặc sku
    @Transactional
    public List<SanPhamResponse> searchSanPhamByTenOrSku(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            return List.of();
        }

        List<SanPham> sanPhamList =
                sanPhamRepository.findByTenSpContainingIgnoreCaseOrMaSkuContainingIgnoreCase(
                        keyword.trim(),
                        keyword.trim()
                );

        return sanPhamList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


}
