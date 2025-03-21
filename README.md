# 🚀 Ecell-Dcrustm Backend - Unified

Welcome to the **Ecell-Dcrustm Spring Boot Backend**! This project provides a robust and scalable user authentication system, supporting JWT-based authentication, role-based access control, and essential user management features.

---

## 📌 Features

- 🛡️ **JWT Authentication** (Access & Refresh Tokens)
- 🔒 **Role-Based Access Control** (Member, Admin, Superuser)
- 📩 **User Registration & Login**
- 📝 **Profile Management** (Update details, change password, etc.)
- 🚀 **Superuser Features** (Role updates, user deletion, token invalidation)
- ✅ **Duplicate Email Handling with proper regex checks**
- 🌐 **REST API for Seamless Integration**

---

## 📂 API Endpoints

### 1. Create User
- **Method:** `POST`
- **URL:** `/api/users`
- **Authentication:** Public
- **Description:** Creates a new user and returns access & refresh tokens.

📌 **Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "photoUrl": "http://example.com/photo.jpg",
  "oauthGoogle": null,
  "role": "MEMBER"
}
```

✅ **Success Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.XXX",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.YYY"
}
```
❌ **Error (409 Conflict - Duplicate Email):**
```json
{
  "message": "User with email john.doe@example.com already exists."
}
```

---

### 2. Check User Existence
- **Method:** `GET`
- **URL:** `/api/users/exists?email=john.doe@example.com`
- **Authentication:** Public
- **Description:** Checks if a user exists by email.

✅ **Success Response (200 OK):**
```json
{
  "exists": true
}
```

----

### 3. Fetch User Details
- **Method:** `GET`
- **URL:** `/api/users/fetch?email=john.doe@example.com`
- **Authentication:** Public
- **Description:** provides the details of a user by email.

✅ **Success Response (200 OK):**
```json
{
  "id": 0,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "photoUrl": "http://dummyimage.com/john.jpg",
  "role": "MEMBER",
  "createdAt": "2025-03-19T16:14:46.877372"
}
```

----

### 4. Login Endpoint
- **Method:** `POST`
- **URL:** `/api/auth/login?provider={provider}`
- **Authentication:** Public
- **Description:** Authenticates a user based on the provider type and returns tokens.

📌 **Request Body:**
Depending on the `provider` value, the request body must include:

For `provider=google`:
```json
{
  "email": "john.doe@example.com",
  "oauthgoogle": "sample_google_token_value"
}
```

For `provider=customEmail`:
```json
{
  "email": "john.doe@example.com",
  "password": "user_plain_text_password"
}
```

✅ **Success Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.xxx",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.yyy"
}
```

❌ **Error Responses:**
- `404 Not Found`: User not found.
- `400 Bad Request`: Missing required fields or unsupported provider.
- `401 Unauthorized`: Invalid credentials.

----

### 5. Update User Role
- **Method:** `PUT`
- **URL:** `/api/users/role`
- **Authentication:** `SUPERUSER Only`

📌 **Request Body:**
```json
{
  "email": "jane.doe@example.com",
  "role": "ADMIN"
}
```

✅ **Success Response (200 OK):**
```json
{
  "id": 5,
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@example.com",
  "role": "ADMIN",
  "createdAt": "2025-03-19T10:15:30"
}
```
❌ **Errors:**
- `401/403` Unauthorized (Non-Superuser access)
- `404` User not found

---

### 6. Update User Details
- **Method:** `PUT`
- **URL:** `/api/users/update`
- **Authentication:** Public (Should be secured later)

📌 **Request Body:**
```json
{
  "email": "john.doe@example.com",
  "photoUrl": "http://example.com/new_photo.jpg",
  "password": "newPassword123"
}
```

✅ **Success Response (200 OK):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "photoUrl": "http://example.com/new_photo.jpg",
  "role": "MEMBER",
  "createdAt": "2025-03-19T09:00:00"
}
```
❌ **Errors:**
- `404` User not found
- `400` Invalid request payload

---

### 7. Delete User
- **Method:** `DELETE`
- **URL:** `/api/users?email=john.doe@example.com`
- **Authentication:** `SUPERUSER Only`

✅ **Success Response (204 No Content)**

❌ **Errors:**
- `401/403` Unauthorized
- `404` User not found

---

### 8. Invalidate User Tokens
- **Method:** `POST`
- **URL:** `/api/auth/invalidate`
- **Authentication:** `SUPERUSER Only`

📌 **Request Body:**
```json
{
  "email": "john.doe@example.com"
}
```

✅ **Success Response (200 OK):**
```json
{
  "message": "User tokens invalidated successfully."
}
```
❌ **Errors:**
- `401/403` Unauthorized
- `404` User not found

---

### 9.  Refresh Access Token
- **Method:** `POST`
- **URL:** `/api/auth/refresh`
- **Authentication:** Public

📌 **Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.YYY"
}
```

✅ **Success Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.NNN",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.MMM"
}
```
❌ **Errors:**
- `401` Invalid/expired refresh token
- `400` Improper request format

---

### 10.  Upload image with caption text
- **Method:** `POST`
- **URL:** `/api/images/upload`
- **Authentication:** `ADMIN` or `SUPERUSER`
- **Bearer Token:** `AUTHORIZATION: BEARER jdshfdlsjfljdlfj.YYY`    
- **Headers:** `Content-Type: multipart/form-data`    


📌 **Request Body:**
```bash
form-data

Key: file (type: File) - Choose an image file (ensure the file is under 10MB.)
Key: caption (type: Text) - Enter your caption text, e.g. "Shiven Saini's picture".


```

✅ **Success Response (200 OK):**
````json
{
"id": 1,
"fileName": "1679324567890-7b2f3d1e-abc1-4e3f-9d2f-1234567890ab.jpg",
"caption": "Shiven Saini's picture",
"creationTime": "2025-03-19T16:14:46.877372",
"imageUrl": "http://192.168.69.65:8080/images/1679324567890-7b2f3d1e-abc1-4e3f-9d2f-1234567890ab.jpg"
}
````

❌ **Errors:**
- `403` Unauthorized access or Forbidden
- 
---

### 11.  Fetch all the stored images along with a public URL.
- **Method:** `GET`
- **URL:** `/api/images`
- **Authentication:** `No Authentication Required`
- **Headers:** `Content-Type: application/json`
- **Request Body:** `No need as such`


✅ **Success Response (200 OK):** An array of stored images.
````json
[
  {
    "id": 1,
    "fileName": "1742546818523-3276872b-68f6-475d-8b57-781564a80e4f.png",
    "caption": "Shiven Saini's picture",
    "creationTime": "2025-03-21T14:16:58.528595",
    "imageUrl": "http://localhost:8080/images/1742546818523-3276872b-68f6-475d-8b57-781564a80e4f.png"
  }
]
````

❌ **Errors:**
- `403`: Forbidden or Improper Request
---

### 12.  Fetch all the images stored after a certain timestamp.
- **Method:** `GET`
- **URL:** `/api/images`
- **Query Param:** `?after=ISO-8601 Formatted Value`
- **Authentication:** `No Authentication Required`
- **Headers:** `Content-Type: application/json`
- **Request Body:** `No need as such`


✅ **Success Response (200 OK):** An array of images stored after specified timestamp. 
````json
[
  {
    "id": 1,
    "fileName": "1742546818523-3276872b-68f6-475d-8b57-781564a80e4f.png",
    "caption": "Shiven Saini's picture",
    "creationTime": "2025-03-21T14:16:58.528595",
    "imageUrl": "http://localhost:8080/images/1742546818523-3276872b-68f6-475d-8b57-781564a80e4f.png"
  }
]
````

❌ **Errors:**
- `403`: Forbidden or Improper Request
---





## 🔐 Authentication & Security
- 🔑 JWT Tokens (Access Token: 8 hours, Refresh Token: 7 days)
- 🔒 **@PreAuthorize** annotations enforce role-based restrictions
- 🛠️ Custom Exception Handling (DuplicateEntityException, UnauthorizedException, etc.)

---

## 📦 Setup & Installation
```bash
# Clone the repository
git clone https://github.com/your-repo/ecell-dcrustm-backend.git
```

---

## 📮 Contact
📧 **Admin:** Shiven Saini  
📩 **Email:** [shiven.career@proton.me](mailto:shiven.career@proton.me)  

---

🔹 **Ecell-Dcrustm** - _Ideate, Integrate, Elevate!_ 🚀

