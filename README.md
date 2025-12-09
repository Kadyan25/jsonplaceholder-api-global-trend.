JSONPlaceholder API Integration – Global Trend Assignment
This project is a Spring Boot REST application that consumes the public JSONPlaceholder API and exposes filtered, structured endpoints for posts and their related user details.
It is built as part of the GLOBAL TREND API Integration Internship assignment to demonstrate API integration, data handling, caching, and error handling skills.

Setup and Run Instructions
Repository
Repository name: jsonplaceholder-api-global-trend.

GitHub URL: https://github.com/Kadyan25/jsonplaceholder-api-global-trend..git

Prerequisites
Java 17 (or compatible version) installed

Maven installed

Internet connection (the app calls the public JSONPlaceholder API)

Clone and build
bash
git clone https://github.com/Kadyan25/jsonplaceholder-api-global-trend..git
cd jsonplaceholder-api-global-trend.

mvn clean install
Run the application
bash
mvn spring-boot:run
By default, the application runs on:

Base URL: http://localhost:8080

You can change the port in application.properties if required.

External Endpoints Used
This application integrates with the JSONPlaceholder public REST API:

GET https://jsonplaceholder.typicode.com/posts – fetches a list of posts

GET https://jsonplaceholder.typicode.com/users – fetches a list of users, used to enrich post details with author information

These two endpoints satisfy the requirement of using at least two different public REST API endpoints.

Internal API Endpoints
All internal endpoints return JSON.

1. List posts with filters
Endpoint

text
GET /api/posts
Query parameters (optional)

userId (Long) – filter posts by user/author ID

titleContains (String) – filter posts whose title contains the given substring (case-insensitive)

Examples

List all posts:

text
GET http://localhost:8080/api/posts
List posts by specific user:

text
GET http://localhost:8080/api/posts?userId=1
List posts whose titles contain a keyword:

text
GET http://localhost:8080/api/posts?titleContains=qui
Combine filters:

text
GET http://localhost:8080/api/posts?userId=1&titleContains=qui
This endpoint satisfies the “list items with filtering options” requirement.

2. Detailed view of a single post
Endpoint

text
GET /api/posts/{id}
Path parameter

id (Long) – ID of the post

Example

text
GET http://localhost:8080/api/posts/1
Response

Returns a detailed view containing:

Post ID, userId, title, body

User name and email (joined from /users data) when available

This endpoint satisfies the “show detailed view for a single item by ID” requirement.

Data Storage and Caching
The application fetches posts and users from JSONPlaceholder and caches them in memory.

A simple in-memory cache (lists of posts and users plus a timestamp) is used inside the PostService.

Cache refresh:

A configurable TTL (time-to-live) in seconds (cache.ttl.seconds, default 300) controls when the data is refetched from the external API.

This approach fulfils the requirement to store/cache fetched data in memory.

Filters Implemented
For the /api/posts endpoint:

userId

Filters the list of posts by the author’s user ID.

titleContains

Performs a case-insensitive substring search on the post title.

Both filters can be combined, and both are optional.

Error Handling
The application includes error handling to cover the cases mentioned in the assignment.

Network failures / timeouts

Network-level issues or timeouts when calling JSONPlaceholder (e.g., host unreachable, request timeouts) are caught and wrapped in a custom ExternalApiException.

The global exception handler returns a 503 Service Unavailable response with a JSON error message.

Invalid or unexpected responses from external API

Non-2xx HTTP status codes or null/invalid response bodies from the external API cause an ExternalApiException, returning 503 Service Unavailable with an appropriate JSON error.

Missing or malformed request fields (client input)

Invalid path or query parameter types (e.g., non-numeric id where a number is expected) are handled via a specific bad-request handler, returning 400 Bad Request with an error message.

If a requested post ID does not exist in the cached data, a ResourceNotFoundException is thrown and mapped to 404 Not Found with a JSON error.

Generic unexpected errors

Any other unhandled exceptions fall back to a generic 500 Internal Server Error JSON response.

These behaviours demonstrate robust error handling for network failures, invalid responses, timeouts, and malformed fields.

Screenshots
Sample screenshots demonstrating the API responses are stored in the screenshots/ folder in the project root.
They illustrate example responses for the list and detail endpoints using typical query parameters.

Assumptions and Notes
JSONPlaceholder is used as the public API because it is open, simple, and does not require authentication.

Only in-memory caching is implemented; no database or file storage is used to keep the project lightweight.

The application does not implement any authentication or authorization, as it is focused purely on API integration and data handling.

The cache TTL is configurable through the cache.ttl.seconds property and defaults to 300 seconds.

Error messages are intentionally simple and JSON-based to keep the API responses clear and easy to test.
