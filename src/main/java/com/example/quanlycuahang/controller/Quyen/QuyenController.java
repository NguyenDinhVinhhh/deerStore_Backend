package com.example.quanlycuahang.controller.Quyen;

import com.example.quanlycuahang.entity.Quyen.Quyen;
import com.example.quanlycuahang.repository.Quyen.IQuyenRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quyen")
@CrossOrigin("*")
public class QuyenController {

    private final IQuyenRepository quyenRepository;

    public QuyenController(IQuyenRepository quyenRepository) {
        this.quyenRepository = quyenRepository;
    }

    @GetMapping
    public List<Quyen> getAllQuyen() {
        return quyenRepository.findAll();
    }
}