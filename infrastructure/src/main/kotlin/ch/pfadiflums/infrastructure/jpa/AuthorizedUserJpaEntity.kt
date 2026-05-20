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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<Role> = mutableSetOf(),

    @Column(name = "profile_photo_url")
    var profilePhotoUrl: String? = null
)
