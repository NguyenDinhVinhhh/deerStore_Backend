package com.example.quanlycuahang.service.Permission;

import com.example.quanlycuahang.entity.Quyen.Quyen;
import com.example.quanlycuahang.entity.VaiTroQuyen.VaiTroQuyen;
import com.example.quanlycuahang.repository.Quyen.IQuyenRepository;
import com.example.quanlycuahang.repository.VaiTroQuyen.VaiTroQuyenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Autowired
    private VaiTroQuyenRepository vaiTroQuyenRepository;

    @Autowired
    private IQuyenRepository quyenRepository;

    public Collection<? extends GrantedAuthority> getAuthoritiesByRole(Integer maVaiTro, boolean isSuperAdmin) {
        Set<String> permissions = new java.util.HashSet<>();

        // 1. Thêm quyền Super Admin
        if (isSuperAdmin) {
            permissions.add("ROLE_SUPER_ADMIN");
        }

        // 2. Thêm quyền dựa trên Vai Trò
        if (maVaiTro != null) {
            List<VaiTroQuyen> rolePermissions = vaiTroQuyenRepository.findByMaVaiTro(maVaiTro);

            // Lấy danh sách các Mã Quyền
            List<Integer> maQuyenList = rolePermissions.stream()
                    .map(VaiTroQuyen::getMaQuyen)
                    .collect(Collectors.toList());

            // Tải tên quyền từ bảng Quyen
            List<Quyen> quyenList = quyenRepository.findAllById(maQuyenList);

            // Thêm tên quyền vào Set
            quyenList.stream()
                    .map(Quyen::getTenQuyen)
                    .forEach(permissions::add);
        }

        // 3. Chuyển đổi thành GrantedAuthority
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}