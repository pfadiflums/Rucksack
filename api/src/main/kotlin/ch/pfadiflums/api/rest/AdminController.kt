package ch.pfadiflums.api.rest

import ch.pfadiflums.application.usecase.InviteUserUseCase
import ch.pfadiflums.domain.model.AuthorizedUser
import ch.pfadiflums.domain.model.Role
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Admin", description = "Invite and manage authorized users (ADMIN only)")
@SecurityRequirement(name = "bearerAuth")
class AdminController(private val inviteUserUseCase: InviteUserUseCase) {

    @PostMapping("/invite")
    @Operation(summary = "Invite a new user by email")
    fun invite(@Valid @RequestBody request: InviteRequest): ResponseEntity<UserResponse> {
        val user = inviteUserUseCase.invite(request.email, request.role)
        return ResponseEntity.status(HttpStatus.CREATED).body(user.toResponse())
    }

    @PutMapping("/users/{email}/role")
    @Operation(summary = "Change an existing user's role")
    fun updateRole(
        @PathVariable email: String,
        @Valid @RequestBody request: RoleUpdateRequest
    ): ResponseEntity<UserResponse> {
        val user = inviteUserUseCase.updateRole(email, request.role)
        return ResponseEntity.ok(user.toResponse())
    }

    @GetMapping("/users")
    @Operation(summary = "List all authorized users")
    fun listUsers(): ResponseEntity<List<UserResponse>> =
        ResponseEntity.ok(inviteUserUseCase.listAll().map { it.toResponse() })
}

data class InviteRequest(
    @field:Email @field:NotBlank val email: String,
    val role: Role = Role.ROLE_LEITER
)

data class RoleUpdateRequest(val role: Role)

data class UserResponse(
    val id: Long?,
    val email: String,
    val pfadiName: String?,
    val role: String
)

private fun AuthorizedUser.toResponse() = UserResponse(
    id = id,
    email = email,
    pfadiName = pfadiName,
    role = role.name
)
