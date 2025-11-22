package com.example.quanlycuahang.service.KhachHang;

import com.example.quanlycuahang.entity.KhachHang.NhomKhachHang;
import com.example.quanlycuahang.repository.KhachHang.NhomKhachHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NhomKhachHangService {

    @Autowired
    private NhomKhachHangRepository nhomKhachHangRepository;

    //hàm lấy danh sách tất cả các hạng
    public List<NhomKhachHang> getAllNhomKhachHang() {
        return nhomKhachHangRepository.findAll();
    }

    // lấy thông tin 1 hạng theo id
    public Optional<NhomKhachHang> getNhomKhachHangById(Integer maNhom) {
        return nhomKhachHangRepository.findById(maNhom);
    }

    ///  hàm cập nhật thông tin hạng
    @Transactional
    public NhomKhachHang updateNhomKhachHang(Integer maNhom, NhomKhachHang updatedNhom) {
        return nhomKhachHangRepository.findById(maNhom)
                .map(nhom -> {

                    if (updatedNhom.getTenNhom() != null) {
                        nhom.setTenNhom(updatedNhom.getTenNhom());
                    }
                    if (updatedNhom.getPhanTramChietKhau() != null) {
                        nhom.setPhanTramChietKhau(updatedNhom.getPhanTramChietKhau());
                    }
                    if (updatedNhom.getNguongChiTieuToiThieu() != null) {
                        nhom.setNguongChiTieuToiThieu(updatedNhom.getNguongChiTieuToiThieu());
                    }
                    if (updatedNhom.getTrangThai() != null) {
                        nhom.setTrangThai(updatedNhom.getTrangThai());
                    }
                    if (updatedNhom.getMoTa() != null) {
                        nhom.setMoTa(updatedNhom.getMoTa());
                    }

                    return nhomKhachHangRepository.save(nhom);
                })
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Nhóm Khách hàng với mã: " + maNhom));
    }

    // Thêm mới 1 hạng
    @Transactional
    public NhomKhachHang createNhomKhachHang(NhomKhachHang nhomKhachHang) {
        return nhomKhachHangRepository.save(nhomKhachHang);
    }
}