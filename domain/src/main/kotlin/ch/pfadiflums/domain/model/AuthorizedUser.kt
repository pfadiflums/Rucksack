package ch.pfadiflums.domain.model

data class AuthorizedUser(
    val id: Long? = null,
    val email: String,
    val pfadiName: String? = null,
    val role: Role
)
