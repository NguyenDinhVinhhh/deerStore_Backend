package com.example.quanlycuahang.repository.Quyen;

import com.example.quanlycuahang.entity.Quyen.Quyen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IQuyenRepository extends JpaRepository<Quyen, Integer> {
    Optional<Quyen> findByTenQuyen(String tenQuyen);
}