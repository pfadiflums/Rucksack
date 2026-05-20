package ch.pfadiflums.infrastructure.adapter

import ch.pfadiflums.domain.model.AuthorizedUser
import ch.pfadiflums.domain.model.Stufe
import ch.pfadiflums.domain.model.StufeLeiter
import ch.pfadiflums.domain.port.StufeLeiterRepository
import ch.pfadiflums.infrastructure.jpa.AuthorizedUserJpaEntity
import ch.pfadiflums.infrastructure.jpa.StufeJpaEntity
import ch.pfadiflums.infrastructure.jpa.StufeJpaRepository
import ch.pfadiflums.infrastructure.jpa.StufeLeiterJpaEntity
import ch.pfadiflums.infrastructure.jpa.StufeLeiterJpaRepository
import ch.pfadiflums.infrastructure.jpa.AuthorizedUserJpaRepository
import org.springframework.stereotype.Repository

@Repository
class StufeLeiterRepositoryAdapter(
    private val jpa: StufeLeiterJpaRepository,
    private val stufeJpa: StufeJpaRepository,
    private val userJpa: AuthorizedUserJpaRepository
) : StufeLeiterRepository {

    override fun findByStufeSlug(stufeSlug: String): List<StufeLeiter> =
        jpa.findByStufeSlug(stufeSlug).map { it.toDomain() }

    override fun save(stufeLeiter: StufeLeiter): StufeLeiter {
        val stufeEntity = stufeJpa.findBySlug(stufeLeiter.stufe.slug)
            ?: stufeJpa.save(stufeLeiter.stufe.toEntity())
        val userEntity = stufeLeiter.authorizedUser.id?.let { userJpa.findById(it).orElse(null) }
            ?: throw NoSuchElementException("AuthorizedUser with id '${stufeLeiter.authorizedUser.id}' not found")
        val entity = StufeLeiterJpaEntity(
            id = stufeLeiter.id,
            stufe = stufeEntity,
            authorizedUser = userEntity,
            sortOrder = stufeLeiter.sortOrder
        )
        return jpa.save(entity).toDomain()
    }

    override fun deleteById(id: Long) =
        jpa.deleteById(id)

    override fun existsByUserIdAndStufeSlug(userId: Long, stufeSlug: String): Boolean =
        jpa.existsByAuthorizedUserIdAndStufeSlug(userId, stufeSlug)

    override fun findById(id: Long): StufeLeiter? =
        jpa.findById(id).orElse(null)?.toDomain()

    private fun StufeLeiterJpaEntity.toDomain() = StufeLeiter(
        id = id,
        stufe = stufe.toDomain(),
        authorizedUser = authorizedUser.toDomain(),
        sortOrder = sortOrder
    )

    private fun StufeJpaEntity.toDomain() = Stufe(
        id = id,
        name = name,
        slug = slug,
        tagline = tagline,
        description = description,
        primaryColor = primaryColor,
        groupPhotoUrl = groupPhotoUrl,
        googleCalendarIframeUrl = googleCalendarIframeUrl
    )

    private fun AuthorizedUserJpaEntity.toDomain() = AuthorizedUser(
        id = id,
        email = email,
        pfadiName = pfadiName,
        roles = roles.toSet(),
        profilePhotoUrl = profilePhotoUrl
    )

    private fun Stufe.toEntity() = StufeJpaEntity(
        id = id,
        name = name,
        slug = slug,
        tagline = tagline,
        description = description,
        primaryColor = primaryColor,
        groupPhotoUrl = groupPhotoUrl,
        googleCalendarIframeUrl = googleCalendarIframeUrl
    )
}
