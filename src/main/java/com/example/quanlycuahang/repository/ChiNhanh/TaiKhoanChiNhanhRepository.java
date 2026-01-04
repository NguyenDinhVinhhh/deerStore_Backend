package com.example.quanlycuahang.repository.ChiNhanh;

import com.example.quanlycuahang.entity.ChiNhanh.TaiKhoanChiNhanh;
import com.example.quanlycuahang.entity.TaiKhoan.TaiKhoan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaiKhoanChiNhanhRepository extends JpaRepository<TaiKhoanChiNhanh, TaiKhoanChiNhanh.TaiKhoanChiNhanhId> {


    List<TaiKhoanChiNhanh> findByMaTk(Integer maTk);

    void deleteByMaTk(Integer maTk);

}
