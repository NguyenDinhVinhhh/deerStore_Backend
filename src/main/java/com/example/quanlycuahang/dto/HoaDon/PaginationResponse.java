package com.example.quanlycuahang.dto.HoaDon;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse<T> {

    private List<T> data;
    private long totalElements;
    private int totalPages;
    private int currentPage;
    private int pageSize;


}