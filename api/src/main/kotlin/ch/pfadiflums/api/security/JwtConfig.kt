package ch.pfadiflums.api.security

import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.OctetSequenceKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.JwtEncoder
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import javax.crypto.spec.SecretKeySpec

@Configuration
class JwtConfig(@Value("\${app.jwt.secret}") private val jwtSecret: String) {

    @Bean
    fun jwtDecoder(): JwtDecoder {
        val key = SecretKeySpec(jwtSecret.toByteArray(Charsets.UTF_8), "HmacSHA256")
        return NimbusJwtDecoder.withSecretKey(key).build()
    }

    @Bean
    fun jwtEncoder(): JwtEncoder {
        // Algorithm must be declared on the JWK so Nimbus can select it during signing
        val key = OctetSequenceKey.Builder(jwtSecret.toByteArray(Charsets.UTF_8))
            .algorithm(JWSAlgorithm.HS256)
            .build()
        val source = ImmutableJWKSet<com.nimbusds.jose.proc.SecurityContext>(JWKSet(key))
        return NimbusJwtEncoder(source)
    }
}
