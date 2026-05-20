package ch.pfadiflums.infrastructure.adapter

import ch.pfadiflums.domain.model.AuthorizedUser
import ch.pfadiflums.domain.port.AuthorizedUserRepository
import ch.pfadiflums.infrastructure.jpa.AuthorizedUserJpaEntity
import ch.pfadiflums.infrastructure.jpa.AuthorizedUserJpaRepository
import org.springframework.stereotype.Repository

@Repository
class AuthorizedUserRepositoryAdapter(
    private val jpa: AuthorizedUserJpaRepository
) : AuthorizedUserRepository {

    override fun findByEmail(email: String): AuthorizedUser? =
        jpa.findByEmail(email)?.toDomain()

    override fun save(user: AuthorizedUser): AuthorizedUser =
        jpa.save(user.toEntity()).toDomain()

    override fun findAll(): List<AuthorizedUser> =
        jpa.findAll().map { it.toDomain() }

    override fun existsByEmail(email: String): Boolean =
        jpa.existsByEmail(email)

    override fun count(): Long =
        jpa.count()

    private fun AuthorizedUserJpaEntity.toDomain() = AuthorizedUser(
        id = id,
        email = email,
        pfadiName = pfadiName,
        role = role
    )

    private fun AuthorizedUser.toEntity() = AuthorizedUserJpaEntity(
        id = id,
        email = email,
        pfadiName = pfadiName,
        role = role
    )
}
