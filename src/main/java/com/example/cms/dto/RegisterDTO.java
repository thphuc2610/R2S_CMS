package com.example.cms.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String description;
    private LocalDateTime createDate;
    private LocalDateTime updateTime;
}
