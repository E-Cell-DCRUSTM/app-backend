package dcrustm.ecell.backend.auth.util

import dcrustm.ecell.backend.user.entity.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {
    private val secretKey = "replaceThisSecretKeyWithAStrongKey" // Replace with your proper secret key

    // Generate access token with an 8-hour expiry
    fun generateAccessToken(user: User): String {
        val claims: MutableMap<String, Any> = HashMap()
        claims["email"] = user.email
        claims["role"] = "ROLE_" + user.role.name
        val now = Date()
        val expiry = Date(now.time + 8 * 60 * 60 * 1000) // 8 hours
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    // Generate refresh token with a 7-day expiry
    fun generateRefreshToken(user: User): String {
        val claims: MutableMap<String, Any> = HashMap()
        claims["email"] = user.email
        val now = Date()
        val expiry = Date(now.time + 7 * 24 * 60 * 60 * 1000) // 7 days
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(expiry)
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()
    }

    fun getClaimsFromToken(token: String): Claims {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .body
    }

    fun getEmailFromToken(token: String): String {
        return getClaimsFromToken(token)["email"].toString()
    }

    fun getExpirationDate(token: String): Date {
        return getClaimsFromToken(token).expiration
    }

}