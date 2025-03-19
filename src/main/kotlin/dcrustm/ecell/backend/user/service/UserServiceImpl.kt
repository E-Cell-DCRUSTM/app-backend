package dcrustm.ecell.backend.user.service

import dcrustm.ecell.backend.DuplicateEntityException
import dcrustm.ecell.backend.user.dto.CreateUserDTO
import dcrustm.ecell.backend.user.entity.Role
import dcrustm.ecell.backend.user.entity.User
import dcrustm.ecell.backend.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserServiceImpl(private val userRepository: UserRepository) : UserService {

    @Transactional
    override fun createUser(dto: CreateUserDTO): User {

        // Check if a user with the given email already exists
        if (userRepository.existsByEmail(dto.email)) {
            throw DuplicateEntityException("User with email ${dto.email} already exists.")
        }

        val user = User(
            firstName = dto.firstName,
            lastName = dto.lastName,
            email = dto.email,
            password = dto.password,
            photoUrl = dto.photoUrl,
            oauthGoogle = dto.oauthGoogle,
            role = dto.role
        )
        return userRepository.save(user)
    }

    @Transactional(readOnly = true)
    override fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    @Transactional
    override fun updateUserRole(email: String, role: Role): User {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found with email: $email")
        user.role = role
        return userRepository.save(user)
    }

    @Transactional
    override fun updateUserInfo(email: String, photoUrl: String?, password: String?): User {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found with email: $email")
        user.photoUrl = photoUrl ?: user.photoUrl
        user.password = password ?: user.password
        return userRepository.save(user)
    }

    @Transactional
    override fun deleteUser(email: String) {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found with email: $email")
        userRepository.delete(user)
    }

}
