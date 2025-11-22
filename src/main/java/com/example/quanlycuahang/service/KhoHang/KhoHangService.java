package com.example.quanlycuahang.service.KhoHang;

import com.example.quanlycuahang.entity.KhoHang.KhoHang;
import com.example.quanlycuahang.repository.KhoHang.KhoHangRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class KhoHangService {

    private final KhoHangRepository khoHangRepository;

    public KhoHangService(KhoHangRepository khoHangRepository) {
        this.khoHangRepository = khoHangRepository;
    }

    // hàm thêm mới hoặc cập nhật kho hàng
    public KhoHang save(KhoHang khoHang) {
        return khoHangRepository.save(khoHang);
    }

    //hàm lấy tất cả kho hàng
    public List<KhoHang> findAll() {
        return khoHangRepository.findAll();
    }

    // hàm lấy kho hàng theo id
    public Optional<KhoHang> findById(Integer maKho) {
        return khoHangRepository.findById(maKho);
    }

    // hàm xóa kho hàng
    public void deleteById(Integer maKho) {
        khoHangRepository.deleteById(maKho);
    }
    //hàm tìm kho hàng theo mã chi nhánh
    public List<KhoHang> findByMaChiNhanh(Integer maChiNhanh) {
        return khoHangRepository.findByMaChiNhanh(maChiNhanh);
    }

    // hàm tìm kho hàng theo mã chi nhánh
    public Integer getMaKhoByMaChiNhanh(Integer maChiNhanh) {
        List<KhoHang> listKhoHang = khoHangRepository.findByMaChiNhanh(maChiNhanh);
        if (listKhoHang.isEmpty()) {
            throw new IllegalArgumentException("Không tìm thấy Kho hàng liên kết với chi nhánh " + maChiNhanh + ".");
        }
        return listKhoHang.get(0).getMaKho();
    }
}