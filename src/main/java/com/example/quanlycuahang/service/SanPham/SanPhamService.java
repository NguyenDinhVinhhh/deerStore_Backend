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
                .hinhAnhUrl(sanPham.getHinhAnh() != null ? imageBaseUrl + sanPham.getHinhAnh() : null)
                .maDanhMuc(sanPham.getMaDanhMuc())
                .tenDanhMuc(danhMuc != null ? danhMuc.getTenDanhMuc() : "Không xác định")
                .trangThai(sanPham.getTrangThai())
                .tonKhoTong(tonKhoDetails)
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

    public SanPhamResponse updateByParams(
            Integer id,
            String tenSP,
            String maSku,
            BigDecimal donGia,
            BigDecimal giaVon,
            String moTa,
            Integer maDanhMuc,
            MultipartFile newHinhAnhFile
    ) {
        SanPham sp = findById(id);

        // Kiểm tra SKU trùng
        sanPhamRepository.findByMaSku(maSku)
                .ifPresent(existing -> {
                    if (!existing.getMaSp().equals(id)) {
                        throw new RuntimeException("Mã SKU đã tồn tại ở sản phẩm khác.");
                    }
                });

        // Kiểm tra danh mục
        danhMucRepository.findById(maDanhMuc)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại: " + maDanhMuc));

        String oldFile = sp.getHinhAnh();
        String newFile = oldFile;

        if (newHinhAnhFile != null && !newHinhAnhFile.isEmpty()) {
            if (oldFile != null) fileUploadService.deleteFile(oldFile);
            newFile = fileUploadService.storeFile(newHinhAnhFile);
        }

        sp.setTenSp(tenSP);
        sp.setMaSku(maSku);
        sp.setDonGia(donGia);
        sp.setGiaVon(giaVon);
        sp.setMoTa(moTa);
        sp.setMaDanhMuc(maDanhMuc);
        sp.setHinhAnh(newFile);

        SanPham updated = sanPhamRepository.save(sp);
        return findByIdResponse(updated.getMaSp());
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
