package ch.pfadiflums.api.security

import ch.pfadiflums.infrastructure.adapter.HitobitoOAuth2UserService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig(
    private val hitobitoOAuth2UserService: HitobitoOAuth2UserService,
    private val oAuth2LoginSuccessHandler: OAuth2LoginSuccessHandler,
    @Value("\${app.frontend-url}") private val frontendUrl: String,
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity, jwtDecoder: JwtDecoder): SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/docs/**", "/actuator/health").permitAll()
                auth.requestMatchers("/admin/**").hasRole("ADMIN")
                auth.anyRequest().authenticated()
            }
            .exceptionHandling { ex ->
                ex.authenticationEntryPoint { _, response, _ ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                }
            }
            .oauth2Login { oauth2 ->
                oauth2.userInfoEndpoint { it.userService(hitobitoOAuth2UserService) }
                oauth2.successHandler(oAuth2LoginSuccessHandler)
                oauth2.failureHandler { _, response, ex ->
                    response.sendRedirect("$frontendUrl/auth/error?reason=${ex.message?.take(100)}")
                }
            }
            .oauth2ResourceServer { rs ->
                rs.jwt { jwt ->
                    jwt.decoder(jwtDecoder)
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
                rs.bearerTokenResolver(bearerTokenResolver())
            }
            .build()
    }

    @Bean
    fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        return JwtAuthenticationConverter().apply {
            setJwtGrantedAuthoritiesConverter { jwt ->
                (jwt.getClaimAsStringList("roles") ?: emptyList())
                    .map { SimpleGrantedAuthority(it) }
            }
        }
    }

    // Resolves JWT from the Authorization: Bearer header, with HttpOnly cookie fallback
    @Bean
    fun bearerTokenResolver(): BearerTokenResolver = BearerTokenResolver { request ->
        val header = request.getHeader("Authorization")
        if (header != null && header.startsWith("Bearer ")) {
            return@BearerTokenResolver header.substring(7)
        }
        request.cookies?.find { it.name == "jwt" }?.value
    }
}
