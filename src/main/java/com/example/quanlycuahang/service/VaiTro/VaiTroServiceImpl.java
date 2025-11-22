package com.example.quanlycuahang.service.VaiTro;

import com.example.quanlycuahang.entity.Quyen.Quyen;
import com.example.quanlycuahang.entity.VaiTro.VaiTro;
import com.example.quanlycuahang.repository.Quyen.IQuyenRepository;
import com.example.quanlycuahang.repository.VaiTro.VaiTroRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VaiTroServiceImpl implements VaiTroService {

    private final VaiTroRepository vaiTroRepository;
    private final IQuyenRepository quyenRepository;

    // ✅ Dùng constructor injection — chuẩn hơn, không cần @Autowired trên field
    public VaiTroServiceImpl(VaiTroRepository vaiTroRepository, IQuyenRepository quyenRepository) {
        this.vaiTroRepository = vaiTroRepository;
        this.quyenRepository = quyenRepository;
    }

    @Override
    @Transactional
    public List<VaiTro> getAll() {
        List<VaiTro> list = vaiTroRepository.findAll();
        list.forEach(v -> v.getDsQuyen().size()); // ép load lazy
        return list;
    }

    @Override
    public VaiTro save(VaiTro vaiTro) {
        return vaiTroRepository.save(vaiTro);
    }

    @Override
    @Transactional
    public VaiTro assignPermission(Integer maVaiTro, Integer maQuyen) {
        VaiTro vaiTro = vaiTroRepository.findById(maVaiTro)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));
        Quyen quyen = quyenRepository.findById(maQuyen)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền"));

        vaiTro.getDsQuyen().add(quyen);
        return vaiTroRepository.save(vaiTro);
    }

    @Override
    @Transactional
    public VaiTro removePermission(Integer maVaiTro, Integer maQuyen) {
        VaiTro vaiTro = vaiTroRepository.findById(maVaiTro)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));
        Quyen quyen = quyenRepository.findById(maQuyen)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quyền"));

        vaiTro.getDsQuyen().remove(quyen);
        return vaiTroRepository.save(vaiTro);
    }

    @Override
    @Transactional
    public Set<String> getPermissionsByRoleId(Integer maVaiTro) {
        VaiTro vaiTro = vaiTroRepository.findById(maVaiTro)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy vai trò"));

        return vaiTro.getDsQuyen()
                .stream()
                .map(Quyen::getTenQuyen)
                .collect(Collectors.toSet());
    }
}
