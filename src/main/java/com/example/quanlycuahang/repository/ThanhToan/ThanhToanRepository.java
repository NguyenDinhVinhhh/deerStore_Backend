package com.example.quanlycuahang.repository.ThanhToan;

import com.example.quanlycuahang.entity.ThanhToan.ThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThanhToanRepository extends JpaRepository<ThanhToan, Integer> {}