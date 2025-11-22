package com.example.quanlycuahang.entity.VaiTro;

import com.example.quanlycuahang.entity.Quyen.Quyen;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vai_tro")
public class VaiTro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ma_vai_tro")
    private Integer maVaiTro;

    @Column(name = "ten_vai_tro", nullable = false, length = 100)
    private String tenVaiTro;

    @Column(name = "mo_ta", length = 255)
    private String moTa;

    // 🔹 Quan hệ nhiều-nhiều với bảng QUYEN
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "vai_tro_quyen",
            joinColumns = @JoinColumn(name = "ma_vai_tro"),
            inverseJoinColumns = @JoinColumn(name = "ma_quyen")
    )
    private Set<Quyen> dsQuyen = new HashSet<>();

    // ===== Constructors =====
    public VaiTro() {}

    public VaiTro(Integer maVaiTro, String tenVaiTro, String moTa) {
        this.maVaiTro = maVaiTro;
        this.tenVaiTro = tenVaiTro;
        this.moTa = moTa;
    }

    public VaiTro(String tenVaiTro, String moTa) {
        this.tenVaiTro = tenVaiTro;
        this.moTa = moTa;
    }

    // ===== Getter & Setter =====
    public Integer getMaVaiTro() {
        return maVaiTro;
    }

    public void setMaVaiTro(Integer maVaiTro) {
        this.maVaiTro = maVaiTro;
    }

    public String getTenVaiTro() {
        return tenVaiTro;
    }

    public void setTenVaiTro(String tenVaiTro) {
        this.tenVaiTro = tenVaiTro;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Set<Quyen> getDsQuyen() {
        return dsQuyen;
    }

    public void setDsQuyen(Set<Quyen> dsQuyen) {
        this.dsQuyen = dsQuyen;
    }

    // ===== toString() =====
    @Override
    public String toString() {
        return "VaiTro{" +
                "maVaiTro=" + maVaiTro +
                ", tenVaiTro='" + tenVaiTro + '\'' +
                ", moTa='" + moTa + '\'' +
                ", soLuongQuyen=" + (dsQuyen != null ? dsQuyen.size() : 0) +
                '}';
    }



}