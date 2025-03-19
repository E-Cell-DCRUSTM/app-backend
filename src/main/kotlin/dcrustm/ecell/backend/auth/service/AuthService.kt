package dcrustm.ecell.backend.auth.service

import dcrustm.ecell.backend.auth.dto.TokenResponse


interface AuthService {
    fun invalidateUserTokens(email: String)
    fun refreshAccessToken(refreshToken: String): TokenResponse
}