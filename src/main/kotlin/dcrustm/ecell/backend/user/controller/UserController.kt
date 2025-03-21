package dcrustm.ecell.backend.user.controller

import dcrustm.ecell.backend.auth.dto.LoginRequest
import dcrustm.ecell.backend.auth.dto.TokenResponse
import dcrustm.ecell.backend.auth.util.JwtUtil
import dcrustm.ecell.backend.user.dto.CreateUserDTO
import dcrustm.ecell.backend.user.dto.RoleUpdateRequest
import dcrustm.ecell.backend.user.dto.UserResponseDTO
import dcrustm.ecell.backend.user.dto.UserUpdateRequest
import dcrustm.ecell.backend.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil
) {
    @PostMapping
    fun createUser(@Valid @RequestBody createUserDTO: CreateUserDTO): ResponseEntity<TokenResponse> {
        val user = userService.createUser(createUserDTO)
// Generate tokens after successful creation
        val accessToken = jwtUtil.generateAccessToken(user)
        val refreshToken = jwtUtil.generateRefreshToken(user)
        return ResponseEntity.ok(TokenResponse(accessToken, refreshToken))
    }

    @GetMapping("/exists")
    fun checkUserExists(@RequestParam email: String): ResponseEntity<Map<String, Boolean>> {
        val exists = userService.getUserByEmail(email) != null
        return ResponseEntity.ok(mapOf("exists" to exists))
    }

    @GetMapping("/fetch")
    fun fetchUserDetails(
        @RequestParam email: String
    ): ResponseEntity<Any> {
        val user = userService.getUserByEmail(email)
        return if (user == null) {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found")
        } else {
            ResponseEntity.ok(UserResponseDTO.from(user))
        }
    }

    @PostMapping("/login")
    fun login(
        @RequestParam provider: String,
        @RequestBody loginRequest: LoginRequest
    ): ResponseEntity<Any> {
        // Retrieve the user from the database using the provided email
        val user = userService.getUserByEmail(loginRequest.email)
            ?: return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found")

        // Decide logic based on the provider query parameter (case-insensitive)
        when (provider.lowercase()) {
            "google" -> {
                // Check if the oauthGoogle field is provided
                if (loginRequest.oauthGoogle.isNullOrBlank()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Missing OAuth Google parameter")
                }
                // Validate that the provided oauthGoogle matches the database value
                if (user.oauthGoogle != loginRequest.oauthGoogle) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid OAuth Google token")
                }
            }
            "customemail" -> {
                // Check if the password is provided
                if (loginRequest.password.isNullOrBlank()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Missing password parameter")
                }
                // Check if the provided password matches the stored (encoded) password
                if (loginRequest.password != user.password) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid password")
                }

            }
            else -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Unsupported provider. Supported values are 'google' and 'customEmail'.")
            }
        }

        // If authentication is successful, generate the access and refresh tokens
        val accessToken = jwtUtil.generateAccessToken(user)
        val refreshToken = jwtUtil.generateRefreshToken(user)

        // Return both tokens in the response
        return ResponseEntity.ok(
            mapOf(
                "accessToken" to accessToken,
                "refreshToken" to refreshToken
            )
        )
    }

    @PutMapping("/role")
    @PreAuthorize("hasRole('SUPERUSER')")
    fun updateUserRole(@Valid @RequestBody roleUpdateRequest: RoleUpdateRequest): ResponseEntity<Any> {
        val updatedUser = userService.updateUserRole(roleUpdateRequest.email, roleUpdateRequest.role)
        return ResponseEntity.ok(updatedUser)
    }

    @PutMapping("/update")
    fun updateUserInfo(@Valid @RequestBody userUpdateRequest: UserUpdateRequest): ResponseEntity<Any> {
        val updatedUser = userService.updateUserInfo(userUpdateRequest.email, userUpdateRequest.photoUrl, userUpdateRequest.password)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping
    @PreAuthorize("hasRole('SUPERUSER')")
    fun deleteUser(@RequestParam email: String): ResponseEntity<Void> {
        userService.deleteUser(email)
        return ResponseEntity.noContent().build()
    }

}