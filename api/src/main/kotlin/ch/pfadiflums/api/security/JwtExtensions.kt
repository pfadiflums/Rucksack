package ch.pfadiflums.api.security

import ch.pfadiflums.domain.model.Role
import org.springframework.security.oauth2.jwt.Jwt

fun Jwt.userId(): Long = (getClaim<Number>("userId")).toLong()

fun Jwt.roles(): Set<Role> = getClaimAsStringList("roles").map { Role.valueOf(it) }.toSet()
