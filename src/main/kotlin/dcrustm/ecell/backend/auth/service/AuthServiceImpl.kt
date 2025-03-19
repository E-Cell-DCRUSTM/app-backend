package dcrustm.ecell.backend.auth.service

import dcrustm.ecell.backend.auth.dto.TokenResponse
import dcrustm.ecell.backend.auth.util.JwtUtil
import dcrustm.ecell.backend.auth.util.TokenStore
import dcrustm.ecell.backend.user.entity.User
import dcrustm.ecell.backend.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthServiceImpl(
    private val jwtUtil: JwtUtil,
    private val userRepository: UserRepository
) : AuthService {

    override fun invalidateUserTokens(email: String) {
        // Mark this email as having invalidated tokens.
        TokenStore.invalidatedEmails.add(email)
    }

    override fun refreshAccessToken(refreshToken: String): TokenResponse {
        // First check if the token is valid
        val email = jwtUtil.getEmailFromToken(refreshToken)

        // If tokens for this email have been invalidated, reject.
        if (TokenStore.invalidatedEmails.contains(email)) {
            throw IllegalArgumentException("Token has been invalidated for this user.")
        }

        val user: User = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("No user found for the provided token.")

        // Check if the refresh token is still valid (throws exception if expired)
        val expiration = jwtUtil.getExpirationDate(refreshToken)
        if (expiration.before(Date())) {
            throw IllegalArgumentException("Refresh token has expired.")
        }

        // Implement periodic refresh of the refresh token: if less than 1 day remains, generate a new one.
        val now = Date()
        val remainingMillis = expiration.time - now.time
        val oneDayMillis = 24 * 60 * 60 * 1000
        val newRefreshToken = if (remainingMillis < oneDayMillis) {
            jwtUtil.generateRefreshToken(user)
        } else refreshToken

        // Always generate a new access token
        val newAccessToken = jwtUtil.generateAccessToken(user)
        return TokenResponse(newAccessToken, newRefreshToken)
    }

}