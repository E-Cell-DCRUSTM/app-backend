package dcrustm.ecell.backend.auth.dto

data class LoginRequest(
    val email: String,
    val password: String? = null,
    val oauthGoogle: String? = null
)
