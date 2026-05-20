package ch.pfadiflums.infrastructure.adapter

import ch.pfadiflums.domain.model.Stufe
import ch.pfadiflums.domain.model.Ubung
import ch.pfadiflums.domain.model.UbungStatus
import ch.pfadiflums.domain.port.UbungRepository
import ch.pfadiflums.infrastructure.jpa.StufeJpaEntity
import ch.pfadiflums.infrastructure.jpa.StufeJpaRepository
import ch.pfadiflums.infrastructure.jpa.UbungJpaEntity
import ch.pfadiflums.infrastructure.jpa.UbungJpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class UbungRepositoryAdapter(
    private val jpa: UbungJpaRepository,
    private val stufeJpa: StufeJpaRepository
) : UbungRepository {

    override fun findPublishedByStufeSlug(stufeSlug: String): List<Ubung> =
        jpa.findByStufeSlugAndStatusOrderByDateAsc(stufeSlug, UbungStatus.PUBLISHED).map { it.toDomain() }

    override fun findNextPublished(stufeSlug: String, fromDate: LocalDate): Ubung? =
        jpa.findTopByStufeSlugAndDateGreaterThanEqualAndStatusOrderByDateAsc(
            stufeSlug, fromDate, UbungStatus.PUBLISHED
        )?.toDomain()

    override fun save(ubung: Ubung): Ubung {
        val stufeEntity = stufeJpa.findBySlug(ubung.stufe.slug)
            ?: stufeJpa.save(ubung.stufe.toEntity())
        val entity = UbungJpaEntity(
            id = ubung.id,
            stufe = stufeEntity,
            date = ubung.date,
            antretenTime = ubung.antretenTime,
            antretenLocation = ubung.antretenLocation,
            abtretenTime = ubung.abtretenTime,
            abtretenLocation = ubung.abtretenLocation,
            motto = ubung.motto,
            tenue = ubung.tenue,
            mitnehmen = ubung.mitnehmen,
            weiteres = ubung.weiteres,
            status = ubung.status,
            createdById = ubung.createdById,
            createdAt = ubung.createdAt,
            updatedAt = ubung.updatedAt
        )
        return jpa.save(entity).toDomain()
    }

    override fun findById(id: Long): Ubung? =
        jpa.findById(id).orElse(null)?.toDomain()

    override fun deleteById(id: Long) =
        jpa.deleteById(id)

    private fun UbungJpaEntity.toDomain() = Ubung(
        id = id,
        stufe = stufe.toDomain(),
        date = date,
        antretenTime = antretenTime,
        antretenLocation = antretenLocation,
        abtretenTime = abtretenTime,
        abtretenLocation = abtretenLocation,
        motto = motto,
        tenue = tenue,
        mitnehmen = mitnehmen,
        weiteres = weiteres,
        status = status,
        createdById = createdById,
        createdAt = createdAt,
        updatedAt = updatedAt
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
