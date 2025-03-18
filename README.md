# Ecell Spring Boot Backend - API Endpoints

This document provides a summary of the implemented API endpoints in the Ecell Spring Boot backend server.

## Endpoints Summary

| Endpoint URL                        | HTTP Method | Description                                                                 | Authentication Requirements                 |
|--------------------------------------|------------|-----------------------------------------------------------------------------|--------------------------------------------|
| `/api/auth/signin`                   | `POST`     | Handles both authentication and account creation via Google Sign-In. If the user does not exist, creates a new user with the default role `Member`. | Not required. This endpoint is public.      |
| `/api/auth/refresh`                   | `POST`     | Accepts a valid refresh token and returns a new pair of access and refresh tokens with updated expiration times. | Not required. Uses the provided refresh token. |
| `/api/admin/users/{userId}/role`      | `PUT`      | Allows an admin user to update a given user's role (e.g., changing from `Member` to `ADMIN` or `INDEPENDENT`). | Requires a valid JWT token with the `ADMIN` role. |

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

## Additional Endpoint Details

### Authentication Endpoints (`/api/auth/`)

#### `/signin`
- **Purpose:** Sign in or sign up users via Google Sign-In data.
- **Notes:**
  - Returns JWT tokens (`accessToken` and `refreshToken`).
  - New users are auto-assigned the `Member` role.

#### `/refresh`
- **Purpose:** Used to refresh tokens before the access token expires.
- **Notes:**
  - Token expiration is enforced, so the system issues new tokens with each valid refresh call.

### Admin Endpoint (`/api/admin/`)

#### User Role Update (`/users/{userId}/role`)
- **Purpose:** Allows an administrator to change user roles.
- **Notes:**
  - Only users with the `ADMIN` role are permitted.
  - This endpoint reinforces role-based authorization to protect sensitive operations.

## Security Considerations

### JWT Authentication
- The application uses a JWT filter that intercepts requests to validate the token.
- Ensure that the `Authorization` header is included for any endpoints that require authentication.

### CSRF Disabled
- With stateless JWT security, CSRF is disabled to simplify API client usage.

### Error Handling
- A `403 Forbidden` response indicates that either:
  - The JWT is invalid.
  - The endpoint’s role requirements (e.g., `ADMIN`) have not been met.

## Development and Testing

### Testing with Postman
- Use the above endpoints by sending JSON payloads and the appropriate headers.
- For protected endpoints (like the admin endpoint), include the header:

  ```text
  Authorization: Bearer <access_token>
  ```

## Database
- As part of integration testing, verify database changes (e.g., updated user roles or new user records) using a MySQL client:

  ```sql
  SELECT * FROM users;
  ```

## Important Configuration Details
- Ensure `application.properties` (or YAML) includes:
  - Proper MySQL configuration, JDBC URL (e.g., pointing to `auth_db`), and Hibernate dialect properties.
  - Correct JWT secret key and expiration times.

This document serves as a reference for the authentication, token refresh, and role-based authorization functions implemented in the Ecell Spring Boot backend.
