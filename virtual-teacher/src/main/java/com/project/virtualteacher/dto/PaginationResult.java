package com.project.virtualteacher.dto;

import lombok.Data;

import java.util.Set;

@Data
public class PaginationResult<E> {
    private int currentPage;
    private int lastPage;
    private int size;
    private long totalRecords;
    Set<E> data;
}
