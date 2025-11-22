package com.example.quanlycuahang.entity.VaiTroQuyen;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Entity
@Table(name = "vai_tro_quyen")
@Data
@IdClass(VaiTroQuyen.VaiTroQuyenId.class)
public class VaiTroQuyen {

    @Id
    @Column(name = "ma_vai_tro")
    private Integer maVaiTro;

    @Id
    @Column(name = "ma_quyen")
    private Integer maQuyen;

    // Lớp nội bộ cho Khóa chính kép
    @Data
    public static class VaiTroQuyenId implements Serializable {
        private Integer maVaiTro;
        private Integer maQuyen;
    }
}