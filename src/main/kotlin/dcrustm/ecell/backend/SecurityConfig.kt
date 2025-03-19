package dcrustm.ecell.backend

import dcrustm.ecell.backend.auth.filters.JwtAuthenticationFilter
import dcrustm.ecell.backend.auth.util.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtUtil: JwtUtil): SecurityFilterChain {


        http.csrf().disable() // Disable CSRF for simplicity (not recommended for production)
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/api/users", "/api/users/exists", "/api/auth/refresh").permitAll() // Allow public access to user creation
                    .anyRequest().authenticated() // Require authentication for all other endpoints
            }
        // Custom JWT filter before the default UsernamePasswordAuthenticationFilter.
        http.addFilterBefore(
            JwtAuthenticationFilter(jwtUtil),
            UsernamePasswordAuthenticationFilter::class.java
        )
        return http.build()
    }
}
