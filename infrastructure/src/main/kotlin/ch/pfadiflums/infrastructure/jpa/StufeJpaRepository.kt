package ch.pfadiflums.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface StufeJpaRepository : JpaRepository<StufeJpaEntity, Long> {
    fun findBySlug(slug: String): StufeJpaEntity?
}
