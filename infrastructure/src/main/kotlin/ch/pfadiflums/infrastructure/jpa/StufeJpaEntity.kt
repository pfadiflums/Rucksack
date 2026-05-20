package ch.pfadiflums.infrastructure.jpa

import jakarta.persistence.*

@Entity
@Table(name = "stufe")
class StufeJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column(nullable = false, unique = true)
    var slug: String,

    @Column
    var tagline: String? = null,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

    @Column(name = "primary_color", nullable = false)
    var primaryColor: String,

    @Column(name = "group_photo_url")
    var groupPhotoUrl: String? = null,

    @Column(name = "google_calendar_iframe_url", columnDefinition = "TEXT")
    var googleCalendarIframeUrl: String? = null
)
