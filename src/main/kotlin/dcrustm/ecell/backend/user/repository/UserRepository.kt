package dcrustm.ecell.backend.user.repository

import dcrustm.ecell.backend.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
    fun findByOauthGoogle(oauthGoogle: String): User?
}