package ch.pfadiflums.infrastructure.jpa

import ch.pfadiflums.domain.model.UbungStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface UbungJpaRepository : JpaRepository<UbungJpaEntity, Long> {
    fun findByStufeSlugAndStatusOrderByDateAsc(slug: String, status: UbungStatus): List<UbungJpaEntity>
    fun findTopByStufeSlugAndDateGreaterThanEqualAndStatusOrderByDateAsc(
        slug: String,
        date: LocalDate,
        status: UbungStatus
    ): UbungJpaEntity?
}
