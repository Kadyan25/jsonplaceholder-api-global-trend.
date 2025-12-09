package com.example.jsonplaceholder_api.controller;

import com.example.jsonplaceholder_api.model.Post;
import com.example.jsonplaceholder_api.model.PostDetailDto;
import com.example.jsonplaceholder_api.service.PostService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // List items with filters
    @GetMapping
    public List<Post> listPosts(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String titleContains) {
        return postService.getPosts(
                Optional.ofNullable(userId),
                Optional.ofNullable(titleContains)
        );
    }

    // Detailed view by ID
    @GetMapping("/{id}")
    public PostDetailDto getPost(@PathVariable Long id) {
        return postService.getPostById(id);
    }
}
