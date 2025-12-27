package com.example.quanlycuahang.controller.DanhMuc;

import com.example.quanlycuahang.dto.DanhMuc.DanhMucRequest;
import com.example.quanlycuahang.entity.DanhMuc.DanhMuc;
import com.example.quanlycuahang.service.DanhMuc.DanhMucService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/danh-muc")
@CrossOrigin("*")
public class DanhMucController {

    @Autowired
    private DanhMucService danhMucService;

    // api lấy danh sách danh mục
    @GetMapping
    public ResponseEntity<List<DanhMuc>> getAll() {
        return ResponseEntity.ok(danhMucService.findAll());
    }

    //api lấy danh mục theo id
    @GetMapping("/{id}")
    public ResponseEntity<DanhMuc> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(danhMucService.findById(id));
    }

    //api taọ mới danh mục
    @PostMapping
//    @PreAuthorize("hasAuthority('THEM_DANH_MUC')")
    public ResponseEntity<DanhMuc> create(@Valid @RequestBody DanhMucRequest request) {
        DanhMuc newDanhMuc = danhMucService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDanhMuc);
    }

    // api cập nhật danh mục
    @PutMapping("/{id}")
//    @PreAuthorize("hasAuthority('SUA_DANH_MUC')")
    public ResponseEntity<DanhMuc> update(@PathVariable Integer id, @Valid @RequestBody DanhMucRequest request) {
        DanhMuc updatedDanhMuc = danhMucService.update(id, request);
        return ResponseEntity.ok(updatedDanhMuc);
    }

    // api xóa danh mục
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('XOA_DANH_MUC')")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        danhMucService.delete(id);
        return ResponseEntity.noContent().build();
    }
}