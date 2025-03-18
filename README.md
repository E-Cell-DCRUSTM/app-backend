# Ecell Spring Boot Backend - API Endpoints

This document provides a summary of the implemented API endpoints in the Ecell Spring Boot backend server.

## Endpoints Summary

| Endpoint URL                        | HTTP Method | Description                                                                 | Authentication Requirements                 |
|--------------------------------------|------------|-----------------------------------------------------------------------------|--------------------------------------------|
| `/api/auth/signin`                   | `POST`     | Handles both authentication and account creation via Google Sign-In. If the user does not exist, creates a new user with the default role `Member`. | Not required. This endpoint is public.      |
| `/api/auth/refresh`                   | `POST`     | Accepts a valid refresh token and returns a new pair of access and refresh tokens with updated expiration times. | Not required. Uses the provided refresh token. |
| `/api/admin/users/{userId}/role`      | `PUT`      | Allows an admin user to update a given user's role (e.g., changing from `Member` to `ADMIN` or `INDEPENDENT`). | Requires a valid JWT token with the `ADMIN` role. |
| `/api/images`                         | `POST`     | Upload an image file. Stores it as a BLOB and returns a Base64 encoded string of the image. | Public (unless security is configured). |
| `/api/images`                         | `GET`      | Retrieve all stored images, including their IDs, file names, and Base64 data. | Public (unless security is configured). |
| `/api/users`                          | `GET`      | Retrieves all users without exposing sensitive data like passwords. | Can be restricted to ADMIN using role-based authorization. |

## Sample Request Bodies

### `/api/auth/signin`
```json
{
  "email": "user@example.com",
  "name": "User Name",
  "password": "dummy"
}
```

### `/api/auth/refresh`
```json
{
  "refreshToken": "your_refresh_token_here"
}
```

### `/api/admin/users/{userId}/role`
```json
{
  "role": "ADMIN"
}
```

## Image Endpoints

### `POST /api/images`
- **Description:**
  - Allows clients to upload an image file using `multipart/form-data`.
  - The image is stored in the database as a BLOB, and the response includes a Base64 encoded string of the image data.
- **Request Details:**
  - HTTP Method: `POST`
  - Content-Type: `multipart/form-data`
  - Body:
    - `file` (Type: File) – attach the desired image file.
- **Response (JSON):**
```json
{
  "id": 1,
  "fileName": "example.jpg",
  "base64Data": "iVBORw0KGgoAAAANSUhEUgAA..."
}
```
- **Authentication:** Public (unless secured otherwise).

### `GET /api/images`
- **Description:**
  - Retrieves a list of all images stored in the database.
  - Each image entry includes its unique ID, file name, and Base64 encoded image data.
- **Request Details:**
  - HTTP Method: `GET`
  - URL: `http://localhost:8080/api/images`
- **Response (JSON):**
```json
[
  {
    "id": 1,
    "fileName": "example.jpg",
    "base64Data": "iVBORw0KGgoAAAANSUhEUgAA..."
  },
  {
    "id": 2,
    "fileName": "anotherImage.png",
    "base64Data": "R0lGODlhPQBEAPeoAJosM...."
  }
]
```
- **Authentication:** Public (unless secured otherwise).

## User Endpoints

### `GET /api/users`
- **Description:**
  - Returns a list of all users present in the `auth_db`.
  - The response maps the `User` entity to a `UserResponse` DTO so that sensitive data like passwords aren’t exposed.
- **Request Details:**
  - HTTP Method: `GET`
  - URL: `http://localhost:8080/api/users`
- **Response (JSON):**
```json
[
  {
    "id": 1,
    "name": "John Doe",
    "email": "john.doe@example.com",
    "role": "MEMBER"
  },
  {
    "id": 2,
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "role": "ADMIN"
  }
]
```
- **Authentication:** Can be restricted to `ADMIN` using `@PreAuthorize("hasRole('ADMIN')")`.

## Security Considerations

### JWT Authentication
- The application uses a JWT filter that intercepts requests to validate the token.
- Ensure that the `Authorization` header is included for any endpoints that require authentication.

### JWT Expiration Settings
- Modify token validity by adjusting configuration properties such as `jwt.expirationMs` and `jwt.refreshExpirationMs`.

### CSRF Disabled
- With stateless JWT security, CSRF is disabled to simplify API client usage.

### Error Handling
- A `403 Forbidden` response indicates that either:
  - The JWT is invalid.
  - The endpoint’s role requirements (e.g., `ADMIN`) have not been met.

## Development and Testing

### Testing with Postman
- **GET endpoints:**
  - Set the URL and ensure headers are set to `Accept: application/json` if needed.
- **POST endpoints (file uploads):**
  - Select `multipart/form-data`, add the `file` key, and choose the file from your system.
- **Secured endpoints:**
  - Include the Authorization header with the JWT in the format:
  ```text
  Authorization: Bearer <access_token>
  ```

## Database
- Verify database changes (e.g., uploaded images or new user records) using a MySQL client:
  ```sql
  SELECT * FROM users;
  SELECT * FROM images;
  ```

## Important Configuration Details
- Ensure `application.properties` (or YAML) includes:
  - Proper MySQL configuration, JDBC URL (e.g., pointing to `auth_db`), and Hibernate dialect properties.
  - Correct JWT secret key and expiration times.

This document serves as a reference for the authentication, image handling, and role-based authorization functions implemented in the Ecell Spring Boot backend.

