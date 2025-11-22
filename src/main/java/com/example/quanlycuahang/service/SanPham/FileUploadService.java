package com.example.quanlycuahang.service.SanPham;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileUploadService {
    // Thư mục lưu trữ hình ảnh (Cần tạo thư mục 'uploads/images' trong root dự án)
    private final Path fileStorageLocation = Paths.get("uploads/images").toAbsolutePath().normalize();

    public FileUploadService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Không thể tạo thư mục lưu trữ file.", ex);
        }
    }

    /**
     * Lưu file hình ảnh vào thư mục cục bộ và trả về tên file duy nhất.
     */
    public String storeFile(MultipartFile file) {
        // Tạo tên file duy nhất để tránh trùng lặp
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null && originalFilename.contains(".") ?
                originalFilename.substring(originalFilename.lastIndexOf(".")) : "";

        String fileName = UUID.randomUUID().toString() + fileExtension;

        try {
            // Kiểm tra file có rỗng không
            if (file.isEmpty()) {
                throw new RuntimeException("File rỗng.");
            }

            // Sao chép file vào thư mục đích (thay thế nếu đã tồn tại)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            // Trả về tên file đã lưu (để lưu vào DB)
            return fileName;

        } catch (IOException ex) {
            throw new RuntimeException("Lưu file không thành công: " + fileName, ex);
        }
    }

    // Thêm hàm xóa file (nếu cần khi xóa/sửa sản phẩm)
    public void deleteFile(String fileName) {
        try {
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.deleteIfExists(targetLocation);
        } catch (IOException ex) {
            // Thường chỉ log lỗi, không ném exception để không làm dừng giao dịch chính
            System.err.println("Không thể xóa file: " + fileName + ". Lỗi: " + ex.getMessage());
        }
    }
}
