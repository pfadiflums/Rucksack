package ch.pfadiflums.api.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.oauth2.jose.jws.MacAlgorithm
import org.springframework.security.oauth2.jwt.JwsHeader
import org.springframework.security.oauth2.jwt.JwtClaimsSet
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.JwtEncoderParameters
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class JwtService(
    private val jwtEncoder: JwtEncoder,
    @Value("\${app.jwt.expiration-seconds:86400}") private val expirationSeconds: Long
) {

    fun generateToken(email: String, roles: List<String>): String {
        val now = Instant.now()
        val header = JwsHeader.with(MacAlgorithm.HS256).build()
        val claims = JwtClaimsSet.builder()
            .issuer("rucksack")
            .subject(email)
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expirationSeconds))
            .claim("roles", roles)
            .build()
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).tokenValue
    }
}
