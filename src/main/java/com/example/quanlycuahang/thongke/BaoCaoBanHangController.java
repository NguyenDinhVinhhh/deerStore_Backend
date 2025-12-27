package com.example.quanlycuahang.thongke;

import com.example.quanlycuahang.dto.HoaDon.TopSanPhamDTO;
import com.example.quanlycuahang.service.BAOCAO.BaoCaoBanHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bao-cao")
public class BaoCaoBanHangController {

    @Autowired
    private BaoCaoBanHangService baoCaoBanHangService;

    /**
     * 📊 API Top sản phẩm bán chạy
     *
     * @param range TODAY | YESTERDAY | 7_DAYS | MONTH | YEAR
     * @param type ORDER | QUANTITY | REVENUE
     * @param maChiNhanh nullable
     * @param limit mặc định 5
     */
    @GetMapping("/top-san-pham")
    public ResponseEntity<?> getTopSanPham(
            @RequestParam String range,
            @RequestParam String type,
            @RequestParam(required = false) Integer maChiNhanh,
            @RequestParam(defaultValue = "10") int limit
    ) {

        List<TopSanPhamDTO> data = baoCaoBanHangService
                .getTopSanPham(range, type, maChiNhanh, limit);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("range", range);
        response.put("type", type);
        response.put("maChiNhanh", maChiNhanh);
        response.put("data", data);

        return ResponseEntity.ok(response);
    }
}