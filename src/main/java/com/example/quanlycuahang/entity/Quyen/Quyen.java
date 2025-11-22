package com.example.quanlycuahang.entity.Quyen;

import jakarta.persistence.*;

@Entity
@Table(name = "quyen")
public class Quyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_quyen")
    private Integer maQuyen;

    @Column(name = "ten_quyen", nullable = false)
    private String tenQuyen;

    @Column(name = "mo_ta")
    private String moTa;

    public Quyen() {}

    public Quyen(String tenQuyen, String moTa) {
        this.tenQuyen = tenQuyen;
        this.moTa = moTa;
    }

    // Getter & Setter
    public Integer getMaQuyen() {
        return maQuyen;
    }

    public void setMaQuyen(Integer maQuyen) {
        this.maQuyen = maQuyen;
    }

    public String getTenQuyen() {
        return tenQuyen;
    }

    public void setTenQuyen(String tenQuyen) {
        this.tenQuyen = tenQuyen;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
}