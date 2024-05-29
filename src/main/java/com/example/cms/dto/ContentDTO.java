package com.example.cms.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ContentDTO {
    private long id;
    private String title;
    private String brief;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updateTime;
    private String sort;
    private long author;
}
