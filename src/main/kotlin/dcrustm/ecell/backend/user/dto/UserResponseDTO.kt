package dcrustm.ecell.backend.user.dto

import dcrustm.ecell.backend.user.entity.Role
import dcrustm.ecell.backend.user.entity.User
import java.time.LocalDateTime

data class UserResponseDTO(
    val id: Long?,
    val firstName: String,
    val lastName: String?,
    val email: String,
    val photoUrl: String?,
    val role: Role,
    val createdAt: LocalDateTime?
) {
    companion object {
        fun from(user: User): UserResponseDTO {
            return UserResponseDTO(
                id = user.id,
                firstName = user.firstName,
                lastName = user.lastName,
                email = user.email,
                photoUrl = user.photoUrl,
                role = user.role,
                createdAt = user.createdAt
            )
        }
    }
}