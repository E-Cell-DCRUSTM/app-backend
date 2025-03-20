package dcrustm.ecell.backend.auth.controller

import dcrustm.ecell.backend.auth.dto.InvalidateTokenRequest
import dcrustm.ecell.backend.auth.dto.TokenRefreshRequest
import dcrustm.ecell.backend.auth.dto.TokenResponse
import dcrustm.ecell.backend.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/invalidate")
    @PreAuthorize("hasRole('SUPERUSER')")
    fun invalidateTokens(@Valid @RequestBody request: InvalidateTokenRequest): ResponseEntity<String> {
        authService.invalidateUserTokens(request.email)
        return ResponseEntity.ok("User tokens invalidated successfully.")
    }

    @PostMapping("/refresh")
    fun refreshToken(@Valid @RequestBody request: TokenRefreshRequest): ResponseEntity<TokenResponse> {
        val tokenResponse = authService.refreshAccessToken(request.refreshToken)
        return ResponseEntity.ok(tokenResponse)
    }



}