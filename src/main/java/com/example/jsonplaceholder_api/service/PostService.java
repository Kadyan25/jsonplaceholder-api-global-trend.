package com.example.jsonplaceholder_api.service;

import com.example.jsonplaceholder_api.client.JsonPlaceholderClient;
import com.example.jsonplaceholder_api.exception.ResourceNotFoundException;
import com.example.jsonplaceholder_api.model.Post;
import com.example.jsonplaceholder_api.model.PostDetailDto;
import com.example.jsonplaceholder_api.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class PostService {

    private final JsonPlaceholderClient client;

    private List<Post> cachedPosts = new ArrayList<>();
    private List<User> cachedUsers = new ArrayList<>();
    private Instant lastFetchTime;

    @Value("${cache.ttl.seconds:300}")
    private long cacheTtlSeconds;

    public PostService(JsonPlaceholderClient client) {
        this.client = client;
    }

    private synchronized void refreshCacheIfStale() {
        if (lastFetchTime == null ||
                Duration.between(lastFetchTime, Instant.now()).getSeconds() > cacheTtlSeconds) {
            cachedPosts = client.fetchPosts();
            cachedUsers = client.fetchUsers();
            lastFetchTime = Instant.now();
        }
    }

    public List<Post> getPosts(Optional<Long> userId, Optional<String> titleContains) {
        refreshCacheIfStale();

        Stream<Post> stream = cachedPosts.stream();

        if (userId.isPresent()) {
            Long uid = userId.get();
            stream = stream.filter(p -> uid.equals(p.getUserId()));
        }

        if (titleContains.isPresent() && !titleContains.get().isBlank()) {
            String key = titleContains.get().toLowerCase();
            stream = stream.filter(p ->
                    p.getTitle() != null && p.getTitle().toLowerCase().contains(key));
        }

        return stream.toList();
    }

    public PostDetailDto getPostById(Long id) {
        refreshCacheIfStale();

        Post post = cachedPosts.stream()
                .filter(p -> id.equals(p.getId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        User user = cachedUsers.stream()
                .filter(u -> post.getUserId().equals(u.getId()))
                .findFirst()
                .orElse(null);

        PostDetailDto dto = new PostDetailDto();
        dto.setId(post.getId());
        dto.setUserId(post.getUserId());
        dto.setTitle(post.getTitle());
        dto.setBody(post.getBody());
        if (user != null) {
            dto.setUserName(user.getName());
            dto.setUserEmail(user.getEmail());
        }
        return dto;
    }
}
