package ch.pfadiflums.application.usecase

import ch.pfadiflums.domain.model.Role
import ch.pfadiflums.domain.model.StufeAccessDeniedException
import ch.pfadiflums.domain.port.StufeLeiterRepository
import org.springframework.stereotype.Service

@Service
class StufePermissionService(private val stufeLeiterRepository: StufeLeiterRepository) {
    fun assertAccess(userId: Long, stufeSlug: String, roles: Set<Role>) {
        if (roles.contains(Role.ROLE_ADMIN) || roles.contains(Role.ROLE_ABTEILUNGSLEITER)) return
        if (!stufeLeiterRepository.existsByUserIdAndStufeSlug(userId, stufeSlug))
            throw StufeAccessDeniedException("No access to Stufe '$stufeSlug'")
    }
}
