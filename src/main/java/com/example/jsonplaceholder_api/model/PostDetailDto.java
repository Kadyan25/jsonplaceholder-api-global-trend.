package com.example.jsonplaceholder_api.model;

import lombok.Data;

@Data
public class PostDetailDto {
    private Long id;
    private Long userId;
    private String title;
    private String body;
    private String userName;
    private String userEmail;
}
