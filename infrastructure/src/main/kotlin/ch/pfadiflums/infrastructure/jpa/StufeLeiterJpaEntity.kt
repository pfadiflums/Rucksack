package ch.pfadiflums.infrastructure.jpa

import jakarta.persistence.*

@Entity
@Table(name = "stufe_leiter")
class StufeLeiterJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "stufe_id", nullable = false)
    var stufe: StufeJpaEntity,

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "authorized_user_id", nullable = false)
    var authorizedUser: AuthorizedUserJpaEntity,

    @Column(name = "sort_order", nullable = false)
    var sortOrder: Int = 0
)
