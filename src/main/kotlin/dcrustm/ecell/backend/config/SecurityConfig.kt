package dcrustm.ecell.backend.config

import dcrustm.ecell.backend.security.CustomUserDetailsService
import dcrustm.ecell.backend.security.JwtAuthenticationFilter
import dcrustm.ecell.backend.security.JwtUtils
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtUtils: JwtUtils
) {

    @Bean
    fun authenticationManager(): AuthenticationManager {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(customUserDetailsService)
        // Using NoOpPasswordEncoder as passwords are stored in plain text
        authProvider.setPasswordEncoder(org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance())
        return ProviderManager(authProvider)
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() } // Disable CSRF for stateless JWT-based authentication
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) } // Stateless session management
            .authorizeHttpRequests { authz ->
                authz
                    .requestMatchers("/api/auth/**").permitAll() // Public endpoints for authentication
                    .requestMatchers("/api/admin/**").hasRole("ADMIN") // Admin-only endpoints
                    .anyRequest().authenticated() // All other requests require authentication
            }
            .addFilterBefore(
                JwtAuthenticationFilter(jwtUtils, customUserDetailsService),
                UsernamePasswordAuthenticationFilter::class.java
            )
        return http.build()
    }

}
