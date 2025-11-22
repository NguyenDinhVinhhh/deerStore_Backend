package com.example.quanlycuahang.repository.ChiNhanh;

import com.example.quanlycuahang.entity.ChiNhanh.ChiNhanh;
// Loại bỏ các import không cần thiết:
// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ChiNhanhRepository extends JpaRepository<ChiNhanh, Integer> {

    Optional<ChiNhanh> findByTenChiNhanh(String tenChiNhanh);

}