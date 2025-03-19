package dcrustm.ecell.backend.auth.filters

import dcrustm.ecell.backend.auth.util.JwtUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtUtil: JwtUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolveToken(request)
        if (token != null) {
            try {
                val claims = jwtUtil.getClaimsFromToken(token)
                val email = claims["email"].toString()

                // Expecting claims["role"] to be a string like "ROLE_SUPERUSER"
                val role = claims["role"].toString()
                val authorities = listOf(SimpleGrantedAuthority(role))
                val authentication = UsernamePasswordAuthenticationToken(email, null, authorities)
                SecurityContextHolder.getContext().authentication = authentication
            } catch (ex: Exception) {
                // If token is invalid, clear the context and optionally log the error
                SecurityContextHolder.clearContext()
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }

}