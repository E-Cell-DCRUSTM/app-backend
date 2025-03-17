import javax.persistence.*

@Entity
@Table(name = "users")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val name: String,

    @Column(unique = true)
    val email: String,

    val password: String,

    @Enumerated(EnumType.STRING)
    val role: Role = Role.MEMBER

)