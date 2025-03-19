package dcrustm.ecell.backend.auth.dto

import jakarta.validation.constraints.NotBlank

data class TokenRefreshRequest(
    @field:NotBlank(message = "Refresh token cannot be blank")
    val refreshToken: String
)