package ch.pfadiflums.infrastructure.adapter

import ch.pfadiflums.domain.model.Stufe
import ch.pfadiflums.domain.port.StufeRepository
import ch.pfadiflums.infrastructure.jpa.StufeJpaEntity
import ch.pfadiflums.infrastructure.jpa.StufeJpaRepository
import org.springframework.stereotype.Repository

@Repository
class StufeRepositoryAdapter(
    private val jpa: StufeJpaRepository
) : StufeRepository {

    override fun findAll(): List<Stufe> =
        jpa.findAll().map { it.toDomain() }

    override fun findBySlug(slug: String): Stufe? =
        jpa.findBySlug(slug)?.toDomain()

    override fun findById(id: Long): Stufe? =
        jpa.findById(id).orElse(null)?.toDomain()

    override fun save(stufe: Stufe): Stufe =
        jpa.save(stufe.toEntity()).toDomain()

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
