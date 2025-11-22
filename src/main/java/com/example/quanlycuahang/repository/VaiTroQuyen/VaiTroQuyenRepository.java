package com.example.quanlycuahang.repository.VaiTroQuyen;

import com.example.quanlycuahang.entity.VaiTroQuyen.VaiTroQuyen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaiTroQuyenRepository extends JpaRepository<VaiTroQuyen, VaiTroQuyen.VaiTroQuyenId> {

    // Hàm custom: Lấy tất cả liên kết Quyền dựa trên Mã Vai Trò
    List<VaiTroQuyen> findByMaVaiTro(Integer maVaiTro);
}
