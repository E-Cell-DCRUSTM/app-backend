package dcrustm.ecell.backend.service

import dcrustm.ecell.backend.model.User
import dcrustm.ecell.backend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class AuthService(private val userRepository: UserRepository) {

    fun signInOrSignUp(user: User): User {
        val existingUser = userRepository.findByEmail(user.email)
        return if (existingUser.isPresent) {
            existingUser.get()
        } else {
            userRepository.save(user)
        }
    }

    fun findUserByEmail(email: String): User? {
        return userRepository.findByEmail(email).orElse(null)
    }

}
