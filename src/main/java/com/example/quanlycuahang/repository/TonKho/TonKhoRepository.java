package com.example.quanlycuahang.repository.TonKho;

import com.example.quanlycuahang.dto.TonKho.SanPhamTonKhoResponse;
import com.example.quanlycuahang.entity.TonKho.TonKho;
import com.example.quanlycuahang.entity.TonKho.TonKhoId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TonKhoRepository extends JpaRepository<TonKho, TonKhoId> {

    // 💡 1. Lấy chi tiết tồn kho theo mã sản phẩm (Tất cả các kho)
    List<TonKho> findByIdMaSp(Integer maSp);

    // 💡 2. Lấy chi tiết tồn kho theo mã kho (Tất cả các sản phẩm)
    List<TonKho> findByIdMaKho(Integer maKho);

    // 💡 3. Tính tổng tồn kho của một sản phẩm (dùng @Query)
    @Query("SELECT SUM(tk.soLuongTon) FROM TonKho tk WHERE tk.id.maSp = :maSp")
    Integer calculateTotalStockByMaSp(@Param("maSp") Integer maSp);


    // 💡 1. Tổng tồn kho theo Danh mục (Tổng hợp theo ma_danh_muc)
    // Trả về List<Object[]> hoặc map sang một DTO (khuyến nghị dùng DTO)
    // Tạm thời dùng Object[]: [maDanhMuc, tenDanhMuc, tongSoLuongTon]
    @Query("SELECT s.maDanhMuc, SUM(tk.soLuongTon) " +
            "FROM TonKho tk JOIN tk.sanPham s " +
            "GROUP BY s.maDanhMuc " +
            "ORDER BY SUM(tk.soLuongTon) DESC")
    List<Object[]> findTotalStockByDanhMuc();

    // 💡 2. Top N Sản phẩm có tồn kho thấp nhất
    // Trả về [maSp, tenSp, tongSoLuongTon]
    @Query(value = "SELECT tk.ma_sp, SUM(tk.so_luong_ton) as totalStock " +
            "FROM ton_kho tk " +
            "GROUP BY tk.ma_sp " +
            "ORDER BY totalStock ASC " +
            "LIMIT :limit", nativeQuery = true) // Dùng Native Query để hỗ trợ LIMIT dễ dàng hơn
    List<Object[]> findTopLowStockProducts(@Param("limit") Integer limit);

    // *Lưu ý: Để truy vấn theo tên danh mục/tên sản phẩm, bạn cần đảm bảo các mối quan hệ (ví dụ: SanPham.danhMuc)
    // đã được định nghĩa trong Entity SanPham, hoặc dùng JOIN trực tiếp như trên.*


    /**
     * Top N Sản phẩm có tồn kho thấp nhất TẠI một kho cụ thể.
     * Trả về [maSp, soLuongTon]
     */
    @Query(value = "SELECT tk.ma_sp, tk.so_luong_ton " +
            "FROM ton_kho tk " +
            "WHERE tk.ma_kho = :maKho " + // Lọc theo Mã Kho
            "ORDER BY tk.so_luong_ton ASC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findTopLowStockProductsByWarehouse(@Param("maKho") Integer maKho, @Param("limit") Integer limit);


//    // Lệnh UPDATE để trừ tồn kho (BÁN HÀNG)
//    @Modifying
//    @Query(value = "UPDATE ton_kho tk SET tk.so_luong = tk.so_luong - :quantity WHERE tk.ma_sp = :maSp AND tk.ma_chi_nhanh = :maCn AND tk.so_luong >= :quantity",
//            nativeQuery = true)
//    int updateStockQuantitySubtract(@Param("quantity") Integer quantity, @Param("maSp") Integer maSp, @Param("maCn") Integer maCn);
//
//    // Lệnh UPDATE để cộng tồn kho (HOÀN TRẢ/HỦY) - NEW METHOD
//    @Modifying
//    @Query(value = "UPDATE ton_kho tk SET tk.so_luong = tk.so_luong + :quantity WHERE tk.ma_sp = :maSp AND tk.ma_chi_nhanh = :maCn",
//            nativeQuery = true)
//    int updateStockQuantityAdd(@Param("quantity") Integer quantity, @Param("maSp") Integer maSp, @Param("maCn") Integer maCn);
//
//    // Phương thức để kiểm tra tồn kho sau khi trừ (hoặc trước khi trừ)
//    Optional<TonKho> findByMaSpAndMaChiNhan(Integer maSp, Integer maChiNhan);

    // Lấy tồn kho theo maSp và maKho (sửa từ maChiNhan)
    // SỬ DỤNG JPQL để truy vấn an toàn qua @EmbeddedId (id.maSp và id.maKho)
    @Query("SELECT tk FROM TonKho tk WHERE tk.id.maSp = :maSp AND tk.id.maKho = :maKho")
    Optional<TonKho> findByMaSpAndMaKho(@Param("maSp") Integer maSp, @Param("maKho") Integer maKho);

    // Cập nhật tồn kho (Trừ số lượng)
    @Modifying
    @Transactional
    @Query("UPDATE TonKho tk SET tk.soLuongTon = tk.soLuongTon - :quantity WHERE tk.id.maSp = :maSp AND tk.id.maKho = :maKho AND tk.soLuongTon >= :quantity")
    int updateStockQuantitySubtract(@Param("maSp") Integer maSp, @Param("maKho") Integer maKho, @Param("quantity") Integer quantity);

    // Cập nhật tồn kho (Cộng số lượng - dùng khi Hủy/Hoàn)
    @Modifying
    @Transactional
    @Query("UPDATE TonKho tk SET tk.soLuongTon = tk.soLuongTon + :quantity WHERE tk.id.maSp = :maSp AND tk.id.maKho = :maKho")
    int updateStockQuantityAdd(@Param("maSp") Integer maSp, @Param("maKho") Integer maKho, @Param("quantity") Integer quantity);



    /**
     * Tìm kiếm tồn kho sản phẩm theo từ khóa (query) TÊN/SKU và lọc theo Mã Kho.
     * Chỉ trả về sản phẩm có số lượng tồn > 0.
     * @param maKho Mã kho cần tìm kiếm.
     * @param query Từ khóa tìm kiếm (tên, SKU).
     * @return Danh sách TonKho khớp điều kiện.
     */
    @Query("SELECT tk FROM TonKho tk " +
            "JOIN tk.sanPham sp " + // JOIN với Entity SanPham
            "WHERE tk.id.maKho = :maKho " +
            "AND tk.soLuongTon > 0 " + // Chỉ tìm sản phẩm còn tồn kho
            "AND (LOWER(sp.tenSp) LIKE LOWER(CONCAT('%', :query, '%')) " + // Tìm kiếm Tên SP (Không phân biệt chữ hoa/thường)
            "OR LOWER(sp.maSku) LIKE LOWER(CONCAT('%', :query, '%')))") // Tìm kiếm Mã SKU (Không phân biệt chữ hoa/thường)
    List<TonKho> searchProductsByQueryAndWarehouse(
            @Param("maKho") Integer maKho,
            @Param("query") String query
    );


    @Query("""
    SELECT new com.example.quanlycuahang.dto.TonKho.SanPhamTonKhoResponse(
        sp.maSp,
        sp.tenSp,
        sp.maSku,
        COALESCE(tk.soLuongTon, 0),
        CASE WHEN tk.id IS NULL THEN false ELSE true END
    )
    FROM SanPham sp
    LEFT JOIN TonKho tk
        ON sp.maSp = tk.id.maSp
        AND tk.id.maKho = :maKho
""")
    List<SanPhamTonKhoResponse> getSanPhamTonKhoTheoKho(
            @Param("maKho") Integer maKho
    );

}