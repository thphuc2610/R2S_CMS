package com.example.cms.dto;

import java.io.Serializable;
import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private List<?> contents;
    private boolean isFirst;
    private boolean isLast;
    private int totalPages;
    private long totalItems;
    private int limit;
    private int no;
}
