import com.example.auth.model.Role
import com.example.auth.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

data class RoleUpdateRequest(
    val role: Role
)

@RestController
@RequestMapping("/api/admin")
class AdminController(private val userRepository: UserRepository) {

    @PutMapping("/users/{userId}/role")
    @PreAuthorize("hasRole('ADMIN')")
    fun updateUserRole(@PathVariable userId: Long, @RequestBody roleUpdate: RoleUpdateRequest): ResponseEntity<String> {
        val userOptional = userRepository.findById(userId)
        if (userOptional.isEmpty) {
            return ResponseEntity.notFound().build()
        }
        val user = userOptional.get()
        val updatedUser = user.copy(role = roleUpdate.role)
        userRepository.save(updatedUser)
        return ResponseEntity.ok("User role updated successfully.")
    }
}
