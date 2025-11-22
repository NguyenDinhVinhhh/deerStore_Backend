package com.example.quanlycuahang.entity.TonKho;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import java.io.Serializable;

@Embeddable
@Data
public class TonKhoId implements Serializable {

    @Column(name = "ma_sp")
    private Integer maSp;

    @Column(name = "ma_kho")
    private Integer maKho;

    // Yêu cầu: Cần có default constructor và overrides hashCode/equals.
    // @Data của Lombok sẽ lo phần này.
}