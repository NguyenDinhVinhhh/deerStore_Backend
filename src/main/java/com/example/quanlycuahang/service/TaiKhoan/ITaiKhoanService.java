package com.example.quanlycuahang.service.TaiKhoan;

import com.example.quanlycuahang.entity.TaiKhoan.TaiKhoan;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface ITaiKhoanService extends UserDetailsService {

    String register(TaiKhoan taiKhoan);
    String login(String username, String password);

}
