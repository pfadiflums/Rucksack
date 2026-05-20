package ch.pfadiflums.api.rest

import ch.pfadiflums.domain.port.AuthorizedUserRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Authenticated user profile")
@SecurityRequirement(name = "bearerAuth")
class UserController(private val authorizedUserRepository: AuthorizedUserRepository) {

    @GetMapping("/me")
    @Operation(summary = "Get the current user's profile")
    fun getMe(@AuthenticationPrincipal jwt: Jwt): ResponseEntity<UserResponse> {
        val user = authorizedUserRepository.findByEmail(jwt.subject)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(user.toResponse())
    }
}
