package com.example.jsonplaceholder_api.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Post {
    private Long userId;
    private Long id;
    private String title;
    private String body;
    
}
