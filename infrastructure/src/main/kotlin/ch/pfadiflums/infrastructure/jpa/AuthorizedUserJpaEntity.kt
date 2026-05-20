package ch.pfadiflums.infrastructure.jpa

import ch.pfadiflums.domain.model.Role
import jakarta.persistence.*

@Entity
@Table(name = "authorized_user")
class AuthorizedUserJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(unique = true, nullable = false)
    var email: String,

    @Column(name = "pfadi_name")
    var pfadiName: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var role: Role
)
