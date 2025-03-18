package dcrustm.ecell.backend.security

import dcrustm.ecell.backend.model.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JwtUtils {

    @Value("\${app.jwtSecret}")
    private lateinit var jwtSecret: String

    @Value("\${app.jwtExpirationMs}")
    private var jwtExpirationMs: Long = 0

    @Value("\${app.jwtRefreshExpirationMs}")
    private var jwtRefreshExpirationMs: Long = 0

    fun generateAccessToken(user: User): String {
        return Jwts.builder()
            .setSubject(user.email)
            .claim("role", user.role.name)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun generateRefreshToken(user: User): String {
        return Jwts.builder()
            .setSubject(user.email)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + jwtRefreshExpirationMs))
            .signWith(SignatureAlgorithm.HS512, jwtSecret)
            .compact()
    }

    fun getEmailFromJwt(token: String): String {
        return getClaimsFromJwt(token).subject
    }

    fun getRoleFromJwt(token: String): String? {
        val claims = getClaimsFromJwt(token)
        return claims["role"] as? String
    }

    fun validateJwtToken(authToken: String): Boolean {
        try {
            val claims = getClaimsFromJwt(authToken)
            if (claims.expiration.before(Date())) {
                return false
            }
            return true
        } catch (e: Exception) {
            println("JWT validation error: ${e.message}")
        }
        return false
    }

    private fun getClaimsFromJwt(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(jwtSecret)
            .parseClaimsJws(token)
            .body
    }

}
