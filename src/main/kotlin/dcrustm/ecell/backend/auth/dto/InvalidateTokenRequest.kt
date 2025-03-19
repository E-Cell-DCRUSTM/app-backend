package dcrustm.ecell.backend.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class InvalidateTokenRequest(
    @field:NotBlank
    @field:Email(message = "Invalid email format", regexp = ".+@.+\\..+")
    val email: String
)
