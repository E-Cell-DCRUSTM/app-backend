package dcrustm.ecell.backend.user.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Email
import dcrustm.ecell.backend.user.entity.Role

data class CreateUserDTO(
    @field:NotBlank(message = "First name cannot be blank")
    val firstName: String,

    val lastName: String? = null,

    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Invalid email format", regexp = ".+@.+\\..+")
    val email: String,

    val password: String? = null,

    val photoUrl: String? = null,

    val oauthGoogle: String? = null,

    val role: Role
)