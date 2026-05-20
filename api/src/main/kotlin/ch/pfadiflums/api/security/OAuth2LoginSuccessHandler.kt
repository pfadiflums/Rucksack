package ch.pfadiflums.api.security

import ch.pfadiflums.domain.port.AuthorizedUserRepository
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class OAuth2LoginSuccessHandler(
    private val jwtService: JwtService,
    private val authorizedUserRepository: AuthorizedUserRepository,
    @Value("\${app.frontend-url}") private val frontendUrl: String,
    @Value("\${app.jwt.expiration-seconds:86400}") private val expirationSeconds: Int
) : SimpleUrlAuthenticationSuccessHandler() {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oauth2User = authentication.principal as OAuth2User
        val email = oauth2User.getAttribute<String>("email")!!
        val roles = authentication.authorities.map { it.authority }

        val user = authorizedUserRepository.findByEmail(email)
            ?: throw IllegalStateException("Authenticated user '$email' not found in database")

        val token = jwtService.generateToken(email, roles, user.id!!)

        // HttpOnly cookie — not readable from JS, safe for web clients
        val cookie = Cookie("jwt", token).apply {
            isHttpOnly = true
            secure = true
            path = "/"
            maxAge = expirationSeconds
        }
        response.addCookie(cookie)

        // Invalidate the OAuth2 session — from here on, JWT is the credential
        request.session?.invalidate()

        response.sendRedirect("$frontendUrl/auth/callback")
    }
}
