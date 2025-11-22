package com.example.quanlycuahang.service.ChiNhanh;

import com.example.quanlycuahang.dto.ChiNhanh.ChiNhanhDto;
import com.example.quanlycuahang.dto.ChiNhanh.ChiNhanhRequest;
import com.example.quanlycuahang.entity.ChiNhanh.ChiNhanh;
import com.example.quanlycuahang.entity.ChiNhanh.TaiKhoanChiNhanh;
import com.example.quanlycuahang.entity.KhoHang.KhoHang;
import com.example.quanlycuahang.exception.ResourceNotFoundException;
import com.example.quanlycuahang.repository.ChiNhanh.ChiNhanhRepository;
import com.example.quanlycuahang.repository.ChiNhanh.TaiKhoanChiNhanhRepository;
import com.example.quanlycuahang.repository.KhoHang.KhoHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BranchService {

    @Autowired
    private TaiKhoanChiNhanhRepository taiKhoanChiNhanhRepository;
    @Autowired
    private ChiNhanhRepository chiNhanhRepository;

    @Autowired
    private  KhoHangRepository khoHangRepository;

    //Hàm mapping
    private ChiNhanhDto mapToDto(ChiNhanh chiNhanh) {
        return ChiNhanhDto.builder()
                .maChiNhanh(chiNhanh.getMaChiNhanh())
                .tenChiNhanh(chiNhanh.getTenChiNhanh())
                .diaChi(chiNhanh.getDiaChi())
                .sdt(chiNhanh.getSdt())
                .nguoiQuanLy(chiNhanh.getNguoiQuanLy())
                .trangThai(chiNhanh.getTrangThai())
                .build();
    }


    // hàm lấy danh sách chi nhánh mà tài khoản được phép truy cập
    public List<ChiNhanhDto> getBranchesByMaTk(Integer maTk) {


        List<TaiKhoanChiNhanh> links = taiKhoanChiNhanhRepository.findByMaTk(maTk);

        List<Integer> maChiNhanhList = links.stream()
                .map(TaiKhoanChiNhanh::getMaChiNhanh)
                .collect(Collectors.toList());

        List<ChiNhanh> branches = chiNhanhRepository.findAllById(maChiNhanhList);


        return branches.stream()
                .map(b -> ChiNhanhDto.builder()
                        .maChiNhanh(b.getMaChiNhanh())
                        .tenChiNhanh(b.getTenChiNhanh())
                        .diaChi(b.getDiaChi())
                        .sdt(b.getSdt())
                        .build())
                .collect(Collectors.toList());
    }

    // hàm lấy tất cả chi nhánh
    public List<ChiNhanhDto> findAll() {
        return chiNhanhRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

   // hàm tìm chi nhánh theo id
    public ChiNhanhDto findById(Integer id) {
        ChiNhanh chiNhanh = chiNhanhRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Chi nhánh với ID: " + id));
        return mapToDto(chiNhanh);
    }

    // hàm thêm chi nhánh mới
    public ChiNhanhDto create(ChiNhanhRequest request) {

        if (chiNhanhRepository.findByTenChiNhanh(request.getTenChiNhanh()).isPresent()) {
            throw new RuntimeException("Tên Chi nhánh đã tồn tại.");
        }

        ChiNhanh chiNhanh = new ChiNhanh();
        chiNhanh.setTenChiNhanh(request.getTenChiNhanh());
        chiNhanh.setDiaChi(request.getDiaChi());
        chiNhanh.setSdt(request.getSdt());
        chiNhanh.setNguoiQuanLy(request.getNguoiQuanLy());

        chiNhanh.setTrangThai(true);

        ChiNhanh savedChiNhanh = chiNhanhRepository.save(chiNhanh);
        return mapToDto(savedChiNhanh);
    }

    // hàm cập nhật chi nhánh
    public ChiNhanhDto update(Integer id, ChiNhanhRequest request) {
        ChiNhanh chiNhanh = chiNhanhRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Chi nhánh với ID: " + id));

        Optional<ChiNhanh> existing = chiNhanhRepository.findByTenChiNhanh(request.getTenChiNhanh());
        if (existing.isPresent() && !existing.get().getMaChiNhanh().equals(id)) {
            throw new RuntimeException("Tên Chi nhánh đã tồn tại bởi chi nhánh khác.");
        }

        chiNhanh.setTenChiNhanh(request.getTenChiNhanh());
        chiNhanh.setDiaChi(request.getDiaChi());
        chiNhanh.setSdt(request.getSdt());
        chiNhanh.setNguoiQuanLy(request.getNguoiQuanLy());
        if (request.getTrangThai() != null) {
            chiNhanh.setTrangThai(request.getTrangThai());
        }

        ChiNhanh updatedChiNhanh = chiNhanhRepository.save(chiNhanh);
        return mapToDto(updatedChiNhanh);
    }

    // hàm xóa chi nhánh
    public void delete(Integer id) {
        if (!chiNhanhRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy Chi nhánh với ID: " + id + " để xóa.");
        }

        chiNhanhRepository.deleteById(id);
    }

    // hàm tìm mã kho hàng bằng mã chi nhánh và lấy kho hàng đầu tiên
    public Integer findMaKhoByMaChiNhanh(Integer maCn) {
        // Do bạn giả định 1:1, bạn có thể tìm Kho đầu tiên thuộc Chi nhánh này.
        Optional<KhoHang> khoHang = khoHangRepository.findByMaChiNhanh(maCn).stream().findFirst();
        return khoHang.map(KhoHang::getMaKho)
                .orElseThrow(() -> new ResourceNotFoundException("Chi nhánh " + maCn + " không có kho hàng được liên kết."));
    }
}