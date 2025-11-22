package com.example.quanlycuahang.repository.DanhMuc;

import com.example.quanlycuahang.entity.DanhMuc.DanhMuc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface DanhMucRepository extends JpaRepository<DanhMuc, Integer> {

    Optional<DanhMuc> findByTenDanhMuc(String tenDanhMuc);
}