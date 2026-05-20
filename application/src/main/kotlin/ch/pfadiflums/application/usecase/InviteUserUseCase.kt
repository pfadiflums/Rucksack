package ch.pfadiflums.application.usecase

import ch.pfadiflums.domain.model.AuthorizedUser
import ch.pfadiflums.domain.model.Role
import ch.pfadiflums.domain.port.AuthorizedUserRepository
import org.springframework.stereotype.Service

@Service
class InviteUserUseCase(
    private val authorizedUserRepository: AuthorizedUserRepository
) {

    fun invite(email: String, roles: Set<Role>): AuthorizedUser {
        if (authorizedUserRepository.existsByEmail(email)) {
            throw IllegalStateException("User '$email' is already invited")
        }
        return authorizedUserRepository.save(AuthorizedUser(email = email, roles = roles))
    }

    fun setRoles(email: String, roles: Set<Role>): AuthorizedUser {
        val user = authorizedUserRepository.findByEmail(email)
            ?: throw NoSuchElementException("User '$email' not found")
        return authorizedUserRepository.save(user.copy(roles = roles))
    }

    fun listAll(): List<AuthorizedUser> = authorizedUserRepository.findAll()
}
