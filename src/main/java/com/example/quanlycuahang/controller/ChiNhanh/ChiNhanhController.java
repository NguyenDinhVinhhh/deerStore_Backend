package com.example.quanlycuahang.controller.ChiNhanh;

import com.example.quanlycuahang.dto.ChiNhanh.ChiNhanhDto;
import com.example.quanlycuahang.dto.ChiNhanh.ChiNhanhRequest;
import com.example.quanlycuahang.service.ChiNhanh.BranchService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chi-nhanh")
@CrossOrigin("*")
public class ChiNhanhController {

    @Autowired
    private BranchService chiNhanhService;

    // api lấy danh sách chi nhánh
    @GetMapping
    public ResponseEntity<List<ChiNhanhDto>> getAllBranches() {
        return ResponseEntity.ok(chiNhanhService.findAll());
    }

    // api lấy chi nhánh theo id
    @GetMapping("/{id}")
    public ResponseEntity<ChiNhanhDto> getBranchById(@PathVariable Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(chiNhanhService.findById(id));
    }

    // api thêm mới chi nhánh
    @PostMapping
    @PreAuthorize("hasAuthority('THEM_CHI_NHANH')")
    public ResponseEntity<ChiNhanhDto> createBranch(@Valid @RequestBody ChiNhanhRequest request) {
        ChiNhanhDto newBranch = chiNhanhService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(newBranch);
    }

    //api câp nhật chi nhánh
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUA_CHI_NHANH')")
    public ResponseEntity<ChiNhanhDto> updateBranch(@PathVariable Integer id, @Valid @RequestBody ChiNhanhRequest request) {
        ChiNhanhDto updatedBranch = chiNhanhService.update(id, request);
        return ResponseEntity.ok(updatedBranch);
    }

    //api xóa chi nhánh
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('XOA_CHI_NHANH')")
    public ResponseEntity<Void> deleteBranch(@PathVariable Integer id) {
        chiNhanhService.delete(id);
        return ResponseEntity.noContent().build();
    }
}