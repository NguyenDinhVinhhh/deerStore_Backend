package com.example.quanlycuahang.repository.KhachHang;


import com.example.quanlycuahang.entity.KhachHang.NhomKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhomKhachHangRepository extends JpaRepository<NhomKhachHang, Integer> {
    List<NhomKhachHang> findAllByOrderByNguongChiTieuToiThieuAsc();
}