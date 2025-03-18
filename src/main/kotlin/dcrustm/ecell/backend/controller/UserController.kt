package dcrustm.ecell.backend.controller

import dcrustm.ecell.backend.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

// Data Transfer Object for User responses
data class UserResponse(
    val id: Long,
    val name: String,
    val email: String,
    val role: String
)

@RestController
@RequestMapping("/api/users")
class UserController(private val userRepository: UserRepository) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserResponse>> {
        val userResponses = userRepository.findAll().map { user ->
            UserResponse(
                id = user.id,
                name = user.name,
                email = user.email,
                role = user.role.name
            )
        }
        return ResponseEntity.ok(userResponses)
    }

}