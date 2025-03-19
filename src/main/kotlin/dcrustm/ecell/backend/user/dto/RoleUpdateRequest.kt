package dcrustm.ecell.backend.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import dcrustm.ecell.backend.user.entity.Role

data class RoleUpdateRequest(
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Email is not valid", regexp = ".+@.+\\..+")
    val email: String,
    val role: Role
)