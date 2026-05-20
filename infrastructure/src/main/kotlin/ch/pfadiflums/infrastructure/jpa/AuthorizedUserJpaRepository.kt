package ch.pfadiflums.infrastructure.jpa

import org.springframework.data.jpa.repository.JpaRepository

interface AuthorizedUserJpaRepository : JpaRepository<AuthorizedUserJpaEntity, Long> {
    fun findByEmail(email: String): AuthorizedUserJpaEntity?
    fun existsByEmail(email: String): Boolean
}
