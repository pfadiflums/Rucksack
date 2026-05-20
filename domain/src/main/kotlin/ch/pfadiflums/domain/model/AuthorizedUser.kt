package ch.pfadiflums.domain.model

data class AuthorizedUser(
    val id: Long? = null,
    val email: String,
    val pfadiName: String? = null,
    val roles: Set<Role>,
    val profilePhotoUrl: String? = null
)
