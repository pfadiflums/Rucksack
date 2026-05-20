package ch.pfadiflums.application.usecase

import ch.pfadiflums.domain.model.Role
import ch.pfadiflums.domain.model.StufeAccessDeniedException
import ch.pfadiflums.domain.model.Ubung
import ch.pfadiflums.domain.model.UbungStatus
import ch.pfadiflums.domain.port.StufeRepository
import ch.pfadiflums.domain.port.UbungRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalTime

data class UbungRequest(
    val date: LocalDate,
    val antretenTime: LocalTime,
    val antretenLocation: String,
    val abtretenTime: LocalTime,
    val abtretenLocation: String,
    val motto: String? = null,
    val tenue: String? = null,
    val mitnehmen: String? = null,
    val weiteres: String? = null
)

@Service
class UbungUseCase(
    private val ubungRepository: UbungRepository,
    private val stufeRepository: StufeRepository,
    private val stufePermissionService: StufePermissionService
) {

    fun create(stufeSlug: String, request: UbungRequest, currentUserId: Long, currentUserRoles: Set<Role>): Ubung {
        stufePermissionService.assertAccess(currentUserId, stufeSlug, currentUserRoles)
        val stufe = stufeRepository.findBySlug(stufeSlug)
            ?: throw NoSuchElementException("Stufe '$stufeSlug' not found")
        val ubung = Ubung(
            stufe = stufe,
            date = request.date,
            antretenTime = request.antretenTime,
            antretenLocation = request.antretenLocation,
            abtretenTime = request.abtretenTime,
            abtretenLocation = request.abtretenLocation,
            motto = request.motto,
            tenue = request.tenue,
            mitnehmen = request.mitnehmen,
            weiteres = request.weiteres,
            createdById = currentUserId
        )
        return ubungRepository.save(ubung)
    }

    fun update(id: Long, request: UbungRequest, currentUserId: Long, currentUserRoles: Set<Role>): Ubung {
        val existing = ubungRepository.findById(id)
            ?: throw NoSuchElementException("Ubung '$id' not found")
        stufePermissionService.assertAccess(currentUserId, existing.stufe.slug, currentUserRoles)
        val updated = existing.copy(
            date = request.date,
            antretenTime = request.antretenTime,
            antretenLocation = request.antretenLocation,
            abtretenTime = request.abtretenTime,
            abtretenLocation = request.abtretenLocation,
            motto = request.motto,
            tenue = request.tenue,
            mitnehmen = request.mitnehmen,
            weiteres = request.weiteres
        )
        return ubungRepository.save(updated)
    }

    fun setStatus(id: Long, status: UbungStatus, currentUserId: Long, currentUserRoles: Set<Role>): Ubung {
        val existing = ubungRepository.findById(id)
            ?: throw NoSuchElementException("Ubung '$id' not found")
        stufePermissionService.assertAccess(currentUserId, existing.stufe.slug, currentUserRoles)
        return ubungRepository.save(existing.copy(status = status))
    }

    fun delete(id: Long, currentUserId: Long, currentUserRoles: Set<Role>) {
        val existing = ubungRepository.findById(id)
            ?: throw NoSuchElementException("Ubung '$id' not found")
        if (!currentUserRoles.contains(Role.ROLE_ADMIN) &&
            !currentUserRoles.contains(Role.ROLE_ABTEILUNGSLEITER) &&
            !currentUserRoles.contains(Role.ROLE_STUFENLEITER)
        ) {
            throw StufeAccessDeniedException("Deleting Ubung requires at least STUFENLEITER role")
        }
        stufePermissionService.assertAccess(currentUserId, existing.stufe.slug, currentUserRoles)
        ubungRepository.deleteById(id)
    }
}
