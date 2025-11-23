package com.example.quanlycuahang.entity.TaiKhoanKhachHang;

import com.example.quanlycuahang.entity.KhachHang.KhachHang;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;


// Import từ jakarta.persistence.* nếu bạn dùng Spring Boot 3+
// import jakarta.persistence.*;

@Entity
@Table(name = "tai_khoan_khach_hang")
public class TaiKhoanKhachHang implements Serializable {

    // Khóa chính (Primary Key) và cũng là Khóa ngoại (Foreign Key)
    // Ánh xạ với cột `ma_kh`
    @Id
    @Column(name = "ma_kh")
    private Integer maKh;

    // Tên đăng nhập
    @Column(name = "ten_dang_nhap", unique = true, nullable = false, length = 100)
    private String tenDangNhap;

    // Mật khẩu (Nên được hash trước khi lưu)
    @Column(name = "mat_khau", nullable = false, length = 255)
    private String matKhau;

    // Ngày tạo tài khoản
    // Sử dụng @ColumnDefault để Hibernate biết rằng giá trị mặc định là CURDATE()
    @Column(name = "ngay_tao", columnDefinition = "DATE DEFAULT (CURDATE())")
    private LocalDate ngayTao;

    // Trạng thái (1 là hoạt động, 0 là khóa)
    // Sử dụng kiểu Boolean hoặc Integer/Byte. Byte phù hợp với TINYINT
    @Column(name = "trang_thai", columnDefinition = "TINYINT DEFAULT 1")
    private Byte trangThai; // Hoặc dùng Boolean nếu bạn muốn dùng true/false

    // Mối quan hệ 1-1 với KhachHang
    // Dùng @MapsId để chỉ định rằng khóa chính của Entity này
    // được lấy từ Entity KhachHang thông qua mối quan hệ @OneToOne
    @OneToOne
    @MapsId // Điều này đảm bảo rằng ma_kh trong TaiKhoanKhachHang được lấy từ KhachHang
    @JoinColumn(name = "ma_kh", referencedColumnName = "ma_kh")
    private KhachHang khachHang;

    // Constructors, Getters, và Setters

    public TaiKhoanKhachHang() {
        // Constructor mặc định
    }

    // Constructor hữu ích (có thể thêm/bớt tùy theo nhu cầu)
    public TaiKhoanKhachHang(String tenDangNhap, String matKhau) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
        this.ngayTao = LocalDate.now();
        this.trangThai = 1;
    }

    // Getters và Setters cho tất cả các thuộc tính
    public Integer getMaKh() {
        return maKh;
    }

    public void setMaKh(Integer maKh) {
        this.maKh = maKh;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Byte getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Byte trangThai) {
        this.trangThai = trangThai;
    }

    public KhachHang getKhachHang() {
        return khachHang;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }
}