package ch.pfadiflums.domain.model

data class Stufe(
    val id: Long? = null,
    val name: String,
    val slug: String,
    val tagline: String? = null,
    val description: String? = null,
    val primaryColor: String,
    val groupPhotoUrl: String? = null,
    val googleCalendarIframeUrl: String? = null
)
