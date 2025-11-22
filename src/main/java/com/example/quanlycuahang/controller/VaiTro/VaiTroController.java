package com.example.quanlycuahang.controller.VaiTro;

import com.example.quanlycuahang.entity.VaiTro.VaiTro;
import com.example.quanlycuahang.service.VaiTro.VaiTroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/vaitro")
@CrossOrigin("*")
public class VaiTroController {

    private final VaiTroService vaiTroService;

    public VaiTroController(VaiTroService vaiTroService) {
        this.vaiTroService = vaiTroService;
    }

    //  1. Lấy danh sách tất cả vai trò
    @GetMapping
    public ResponseEntity<List<VaiTro>> getAllVaiTro() {
        List<VaiTro> list = vaiTroService.getAll();
        return ResponseEntity.ok(list);
    }

    //  2. Tạo mới hoặc cập nhật vai trò
    @PostMapping
    public ResponseEntity<VaiTro> createVaiTro(@RequestBody VaiTro vaiTro) {
        VaiTro saved = vaiTroService.save(vaiTro);
        return ResponseEntity.ok(saved);
    }

    //  3. Gán quyền cho vai trò
    @PostMapping("/{maVaiTro}/assign/{maQuyen}")
    public ResponseEntity<VaiTro> assignPermission(
            @PathVariable Integer maVaiTro,
            @PathVariable Integer maQuyen
    ) {
        VaiTro updated = vaiTroService.assignPermission(maVaiTro, maQuyen);
        return ResponseEntity.ok(updated);
    }

    //  4. Gỡ quyền khỏi vai trò
    @DeleteMapping("/{maVaiTro}/remove/{maQuyen}")
    public ResponseEntity<VaiTro> removePermission(
            @PathVariable Integer maVaiTro,
            @PathVariable Integer maQuyen
    ) {
        VaiTro updated = vaiTroService.removePermission(maVaiTro, maQuyen);
        return ResponseEntity.ok(updated);
    }

    //  5. Lấy danh sách quyền của vai trò
    @GetMapping("/{maVaiTro}/permissions")
    public ResponseEntity<Set<String>> getPermissionsByRoleId(
            @PathVariable Integer maVaiTro
    ) {
        Set<String> permissions = vaiTroService.getPermissionsByRoleId(maVaiTro);
        return ResponseEntity.ok(permissions);
    }
}
