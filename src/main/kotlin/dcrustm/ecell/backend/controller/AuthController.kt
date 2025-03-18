package dcrustm.ecell.backend.controller

import dcrustm.ecell.backend.model.Role
import dcrustm.ecell.backend.model.User
import dcrustm.ecell.backend.security.JwtUtils
import dcrustm.ecell.backend.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

data class AuthRequest(
    val email: String,
    val name: String,
    val password: String? = null
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String = "Bearer"
)

data class RefreshRequest(
    val refreshToken: String
)

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService,
    private val jwtUtils: JwtUtils
) {

    @PostMapping("/signin")
    fun signIn(@RequestBody authRequest: AuthRequest): ResponseEntity<AuthResponse> {
        // If user exists, load it; else create it with default role MEMBER.
        val user = authService.signInOrSignUp(
            User(
                name = authRequest.name,
                email = authRequest.email,
                password = authRequest.password ?: "",
                role = Role.MEMBER
            )
        )
        val accessToken = jwtUtils.generateAccessToken(user)
        val refreshToken = jwtUtils.generateRefreshToken(user)
        return ResponseEntity.ok(AuthResponse(accessToken, refreshToken))
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody refreshRequest: RefreshRequest): ResponseEntity<AuthResponse> {
        if (!jwtUtils.validateJwtToken(refreshRequest.refreshToken)) {
            return ResponseEntity.badRequest().build()
        }
        val email = jwtUtils.getEmailFromJwt(refreshRequest.refreshToken)
        val user = authService.findUserByEmail(email)
            ?: return ResponseEntity.badRequest().build()
        val newAccessToken = jwtUtils.generateAccessToken(user)
        val newRefreshToken = jwtUtils.generateRefreshToken(user)
        return ResponseEntity.ok(AuthResponse(newAccessToken, newRefreshToken))
    }
}
