package ch.pfadiflums.infrastructure.jpa

import ch.pfadiflums.domain.model.UbungStatus
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Table(name = "ubung")
@EntityListeners(AuditingEntityListener::class)
class UbungJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stufe_id", nullable = false)
    var stufe: StufeJpaEntity,

    @Column(nullable = false)
    var date: LocalDate,

    @Column(name = "antreten_time", nullable = false)
    var antretenTime: LocalTime,

    @Column(name = "antreten_location", nullable = false)
    var antretenLocation: String,

    @Column(name = "abtreten_time", nullable = false)
    var abtretenTime: LocalTime,

    @Column(name = "abtreten_location", nullable = false)
    var abtretenLocation: String,

    @Column
    var motto: String? = null,

    @Column(columnDefinition = "TEXT")
    var tenue: String? = null,

    @Column(columnDefinition = "TEXT")
    var mitnehmen: String? = null,

    @Column(columnDefinition = "TEXT")
    var weiteres: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: UbungStatus = UbungStatus.DRAFT,

    @Column(name = "created_by_id", nullable = false)
    var createdById: Long,

    @CreatedDate
    @Column(name = "created_at", nullable = true)
    var createdAt: Instant? = null,

    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    var updatedAt: Instant? = null
)
