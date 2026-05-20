package ch.pfadiflums.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface StufeLeiterJpaRepository : JpaRepository<StufeLeiterJpaEntity, Long> {
    fun findByStufeSlug(slug: String): List<StufeLeiterJpaEntity>
    fun existsByAuthorizedUserIdAndStufeSlug(userId: Long, stufeSlug: String): Boolean
}
