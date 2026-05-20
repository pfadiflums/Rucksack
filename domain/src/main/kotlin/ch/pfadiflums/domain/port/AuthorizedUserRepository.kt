package ch.pfadiflums.domain.port

import ch.pfadiflums.domain.model.AuthorizedUser

interface AuthorizedUserRepository {
    fun findByEmail(email: String): AuthorizedUser?
    fun save(user: AuthorizedUser): AuthorizedUser
    fun findAll(): List<AuthorizedUser>
    fun existsByEmail(email: String): Boolean
    fun count(): Long
}
