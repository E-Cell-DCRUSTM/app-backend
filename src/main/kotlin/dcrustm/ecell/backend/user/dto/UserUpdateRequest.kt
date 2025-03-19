package dcrustm.ecell.backend.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank


data class UserUpdateRequest(
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Email is not valid", regexp = ".+@.+\\..+")
    val email: String,
    val photoUrl: String? = null,
    val password: String? = null
)