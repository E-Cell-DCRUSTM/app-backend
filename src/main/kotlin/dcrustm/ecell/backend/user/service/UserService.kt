package dcrustm.ecell.backend.user.service

import dcrustm.ecell.backend.user.dto.CreateUserDTO
import dcrustm.ecell.backend.user.entity.Role
import dcrustm.ecell.backend.user.entity.User

interface UserService {
    fun createUser(dto: CreateUserDTO): User
    fun getUserByEmail(email: String): User?
    fun updateUserRole(email: String, role: Role): User
    fun updateUserInfo(email: String, photoUrl: String?, password: String?): User
    fun deleteUser(email: String)
}