package com.example.quanlycuahang.config;

import com.example.quanlycuahang.entity.Quyen.Quyen;
import com.example.quanlycuahang.repository.Quyen.IQuyenRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(IQuyenRepository quyenRepository) {
        return args -> {
            List<String> defaultPermissions = List.of(
                    "XEM_NHAN_VIEN",
                    "THEM_NHAN_VIEN",
                    "SUA_NHAN_VIEN",
                    "XOA_NHAN_VIEN",
                    "THEM_SAN_PHAM",
                    "SUA_SAN_PHAM",
                    "XOA_SAN_PHAM",
                    "XEM_HOA_DON",
                    "THEM_HOA_DON",
                    "SUA_HOA_DON",
                    "XEM",
                    "THEM_DANH_MUC",
                    "SUA_DANH_MUC",
                    "XOA_DANH_MUC",
                    "THEM_CHI_NHANH",
                    "SUA_CHI_NHANH",
                    "XOA_CHI_NHANH",
                    "XOA_DANH_MUC",
                    "THEM_KHUYEN_MAI",
                    "SUA_KHUYEN_MAI",
                    "XOA_KHUYEN_MAI"
            );

            for (String permission : defaultPermissions) {
                if (quyenRepository.findByTenQuyen(permission).isEmpty()) {
                    Quyen q = new Quyen(permission, "Quyền " + permission.toLowerCase());
                    quyenRepository.save(q);
                }
            }

            System.out.println(" Dữ liệu bảng QUYEN đã được khởi tạo!");
        };
    }
}