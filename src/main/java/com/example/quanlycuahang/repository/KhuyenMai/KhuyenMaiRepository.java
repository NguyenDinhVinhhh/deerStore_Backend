package com.example.quanlycuahang.repository.KhuyenMai;

import com.example.quanlycuahang.entity.KhuyenMai.KhuyenMai;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Integer> {

    Optional<KhuyenMai> findByMaCode(String maCode);
}