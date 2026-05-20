package ch.pfadiflums.application.usecase

import ch.pfadiflums.domain.model.Role
import ch.pfadiflums.domain.model.StufeLeiter
import ch.pfadiflums.domain.port.AuthorizedUserRepository
import ch.pfadiflums.domain.port.StufeLeiterRepository
import ch.pfadiflums.domain.port.StufeRepository
import org.springframework.stereotype.Service

@Service
class StufeLeiterUseCase(
    private val stufeLeiterRepository: StufeLeiterRepository,
    private val stufeRepository: StufeRepository,
    private val authorizedUserRepository: AuthorizedUserRepository,
    private val stufePermissionService: StufePermissionService
) {

    fun assign(
        stufeSlug: String,
        userEmail: String,
        sortOrder: Int,
        currentUserId: Long,
        currentUserRoles: Set<Role>
    ): StufeLeiter {
        stufePermissionService.assertAccess(currentUserId, stufeSlug, currentUserRoles)
        val stufe = stufeRepository.findBySlug(stufeSlug)
            ?: throw NoSuchElementException("Stufe '$stufeSlug' not found")
        val user = authorizedUserRepository.findByEmail(userEmail)
            ?: throw NoSuchElementException("User '$userEmail' not found")
        return stufeLeiterRepository.save(
            StufeLeiter(stufe = stufe, authorizedUser = user, sortOrder = sortOrder)
        )
    }

    fun remove(id: Long, stufeSlug: String, currentUserId: Long, currentUserRoles: Set<Role>) {
        stufePermissionService.assertAccess(currentUserId, stufeSlug, currentUserRoles)
        stufeLeiterRepository.deleteById(id)
    }
}
