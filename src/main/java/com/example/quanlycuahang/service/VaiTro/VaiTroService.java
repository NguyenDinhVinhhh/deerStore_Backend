package com.example.quanlycuahang.service.VaiTro;

import com.example.quanlycuahang.entity.VaiTro.VaiTro;
import java.util.List;
import java.util.Set;

public interface VaiTroService {
    List<VaiTro> getAll();
    VaiTro save(VaiTro vaiTro);
    VaiTro assignPermission(Integer maVaiTro, Integer maQuyen);
    VaiTro removePermission(Integer maVaiTro, Integer maQuyen);
    Set<String> getPermissionsByRoleId(Integer maVaiTro);
}
