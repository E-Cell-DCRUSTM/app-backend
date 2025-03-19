package dcrustm.ecell.backend.user.controller

import dcrustm.ecell.backend.auth.dto.TokenResponse
import dcrustm.ecell.backend.auth.util.JwtUtil
import dcrustm.ecell.backend.user.dto.CreateUserDTO
import dcrustm.ecell.backend.user.dto.RoleUpdateRequest
import dcrustm.ecell.backend.user.dto.UserUpdateRequest
import dcrustm.ecell.backend.user.service.UserService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.security.access.prepost.PreAuthorize

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val jwtUtil: JwtUtil
) {
    @PostMapping
    fun createUser(@Valid @RequestBody createUserDTO: CreateUserDTO): ResponseEntity<TokenResponse> {
        val user = userService.createUser(createUserDTO)
// Generate tokens after successful creation
        val accessToken = jwtUtil.generateAccessToken(user)
        val refreshToken = jwtUtil.generateRefreshToken(user)
        return ResponseEntity.ok(TokenResponse(accessToken, refreshToken))
    }

    @GetMapping("/exists")
    fun checkUserExists(@RequestParam email: String): ResponseEntity<Map<String, Boolean>> {
        val exists = userService.getUserByEmail(email) != null
        return ResponseEntity.ok(mapOf("exists" to exists))
    }

    @PutMapping("/role")
    @PreAuthorize("hasRole('SUPERUSER')")
    fun updateUserRole(@Valid @RequestBody roleUpdateRequest: RoleUpdateRequest): ResponseEntity<Any> {
        val updatedUser = userService.updateUserRole(roleUpdateRequest.email, roleUpdateRequest.role)
        return ResponseEntity.ok(updatedUser)
    }

    @PutMapping("/update")
    fun updateUserInfo(@Valid @RequestBody userUpdateRequest: UserUpdateRequest): ResponseEntity<Any> {
        val updatedUser = userService.updateUserInfo(userUpdateRequest.email, userUpdateRequest.photoUrl, userUpdateRequest.password)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping
    @PreAuthorize("hasRole('SUPERUSER')")
    fun deleteUser(@RequestParam email: String): ResponseEntity<Void> {
        userService.deleteUser(email)
        return ResponseEntity.noContent().build()
    }

}