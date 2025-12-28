package com.example.quanlycuahang.repository.KhachHang;

import com.example.quanlycuahang.entity.KhachHang.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {

    Optional<KhachHang> findBySdt(String sdt);
    Optional<KhachHang> findByMaKh(Integer makh);
    List<KhachHang> findBySdtContainingOrHoTenContainingIgnoreCase(String sdtKeyword, String hoTenKeyword);
}
