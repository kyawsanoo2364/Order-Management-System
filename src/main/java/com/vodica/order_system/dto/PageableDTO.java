package com.vodica.order_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageableDTO<T> {
    private List<T> data;
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int pageSize;
    private boolean hasNext;
    private boolean hasPrevious;

    public PageableDTO( Page<T> page){
        this.data = page.getContent();
        this.currentPage = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalItems = page.getTotalElements();
        this.pageSize = page.getSize();
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();
    }
}
