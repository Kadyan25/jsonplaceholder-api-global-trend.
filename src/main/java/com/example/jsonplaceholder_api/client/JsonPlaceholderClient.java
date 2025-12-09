package com.example.jsonplaceholder_api.client;

import com.example.jsonplaceholder_api.exception.ExternalApiException;
import com.example.jsonplaceholder_api.model.Post;
import com.example.jsonplaceholder_api.model.User;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Component
public class JsonPlaceholderClient {

    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    private final RestTemplate restTemplate;

    public JsonPlaceholderClient(RestTemplateBuilder builder) {
        this.restTemplate = builder
                .setConnectTimeout(Duration.ofSeconds(3))
                .setReadTimeout(Duration.ofSeconds(3))
                .build();
    }

    public List<Post> fetchPosts() {
        try {
            ResponseEntity<Post[]> response =
                    restTemplate.getForEntity(BASE_URL + "/posts", Post[].class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new ExternalApiException("Invalid response while fetching posts");
            }

            return Arrays.asList(response.getBody());
        } catch (ResourceAccessException ex) {
            throw new ExternalApiException("Network error while calling posts API");
        } catch (RestClientException ex) {
            throw new ExternalApiException("Unexpected error while calling posts API");
        }
    }

    public List<User> fetchUsers() {
        try {
            ResponseEntity<User[]> response =
                    restTemplate.getForEntity(BASE_URL + "/users", User[].class);

            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new ExternalApiException("Invalid response while fetching users");
            }

            return Arrays.asList(response.getBody());
        } catch (ResourceAccessException ex) {
            throw new ExternalApiException("Network error while calling users API");
        } catch (RestClientException ex) {
            throw new ExternalApiException("Unexpected error while calling users API");
        }
    }
}
