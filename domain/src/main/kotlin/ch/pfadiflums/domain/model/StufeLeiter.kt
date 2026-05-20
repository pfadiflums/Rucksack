package ch.pfadiflums.domain.model

data class StufeLeiter(
    val id: Long? = null,
    val stufe: Stufe,
    val authorizedUser: AuthorizedUser,
    val sortOrder: Int = 0
)
