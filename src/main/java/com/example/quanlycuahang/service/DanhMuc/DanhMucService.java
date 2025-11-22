package com.example.quanlycuahang.service.DanhMuc;

import com.example.quanlycuahang.dto.DanhMuc.DanhMucRequest;
import com.example.quanlycuahang.entity.DanhMuc.DanhMuc;
import com.example.quanlycuahang.repository.DanhMuc.DanhMucRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DanhMucService {

    @Autowired
    private DanhMucRepository danhMucRepository;

    //Hàm lấy tât cả danh mục
    public List<DanhMuc> findAll() {
        return danhMucRepository.findAll();
    }

    //Hàm tìm kiếm danh mục theo mã
    public DanhMuc findById(Integer id) {
        return danhMucRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Danh mục có ID: " + id));
    }

    // Hàm tạo danh mục
    public DanhMuc create(DanhMucRequest request) {
        if (danhMucRepository.findByTenDanhMuc(request.getTenDanhMuc()).isPresent()) {
            throw new RuntimeException("Tên danh mục đã tồn tại.");
        }

        DanhMuc danhMuc = new DanhMuc();
        danhMuc.setTenDanhMuc(request.getTenDanhMuc());
        danhMuc.setMoTa(request.getMoTa());
        danhMuc.setTrangThai(request.getTrangThai());

        return danhMucRepository.save(danhMuc);
    }

    // Hàm cập nhật danh mục
    public DanhMuc update(Integer id, DanhMucRequest request) {
        DanhMuc danhMuc = findById(id);
        Optional<DanhMuc> existing = danhMucRepository.findByTenDanhMuc(request.getTenDanhMuc());
        if (existing.isPresent() && !existing.get().getMaDanhMuc().equals(id)) {
            throw new RuntimeException("Tên danh mục đã tồn tại bởi danh mục khác.");
        }
        danhMuc.setTenDanhMuc(request.getTenDanhMuc());
        danhMuc.setMoTa(request.getMoTa());
        danhMuc.setTrangThai(request.getTrangThai());

        return danhMucRepository.save(danhMuc);
    }

    //Hàm xóa danh mục
    public void delete(Integer id) {
        DanhMuc danhMuc = findById(id);
        danhMucRepository.delete(danhMuc);
    }
}