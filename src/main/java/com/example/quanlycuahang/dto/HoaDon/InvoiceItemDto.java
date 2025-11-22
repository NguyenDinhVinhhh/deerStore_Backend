package com.example.quanlycuahang.dto.HoaDon;

import com.fasterxml.jackson.annotation.JsonProperty; // 1. Nhớ Import dòng này
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InvoiceItemDto {


    @JsonProperty("ma_sp")
    private Integer maSp;


    @JsonProperty("so_luong")
    private Integer soLuong;



    public Integer getMaSp() {
        return maSp;
    }

    public void setMaSp(Integer maSp) {
        this.maSp = maSp;
    }

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }


}