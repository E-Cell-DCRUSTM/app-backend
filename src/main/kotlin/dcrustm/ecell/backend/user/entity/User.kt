package dcrustm.ecell.backend.user.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Email
import java.time.LocalDateTime
import org.hibernate.annotations.CreationTimestamp
import org.springframework.data.jpa.domain.support.AuditingEntityListener

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(
    name = "users",
    uniqueConstraints = [
        UniqueConstraint(columnNames = ["email"]),
        UniqueConstraint(columnNames = ["oauth_google"])
    ]
)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "first_name", nullable = false)
    @field:NotBlank(message = "First name must not be empty")
    var firstName: String,

    @Column(name = "last_name")
    var lastName: String? = null,

    @Column(nullable = false, unique = true)
    @field:NotBlank(message = "Email must not be empty")
    @field:Email(message = "Invalid email format")
    var email: String,

    @Column
    var password: String? = null,

    @Column(name = "photo_url")
    var photoUrl: String? = null,

    @Column(name = "oauth_google", unique = true)
    var oauthGoogle: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role,

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    var createdAt: LocalDateTime? = null
)
