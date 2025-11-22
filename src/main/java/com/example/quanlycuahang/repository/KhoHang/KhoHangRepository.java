package com.example.quanlycuahang.repository.KhoHang;

import com.example.quanlycuahang.entity.KhoHang.KhoHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface KhoHangRepository extends JpaRepository<KhoHang, Integer> {
    List<KhoHang> findByMaChiNhanh(Integer maChiNhanh);
}