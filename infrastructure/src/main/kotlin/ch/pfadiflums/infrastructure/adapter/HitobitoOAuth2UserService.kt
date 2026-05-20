package ch.pfadiflums.infrastructure.adapter

import ch.pfadiflums.domain.port.AuthorizedUserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class HitobitoOAuth2UserService(
    private val authorizedUserRepository: AuthorizedUserRepository
) : DefaultOAuth2UserService() {

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oauth2User = super.loadUser(userRequest)

        val email = oauth2User.getAttribute<String>("email")
            ?: throw OAuth2AuthenticationException(
                OAuth2Error("missing_email"), "Hitobito did not return an email address"
            )

        val user = authorizedUserRepository.findByEmail(email)
            ?: throw OAuth2AuthenticationException(
                OAuth2Error("not_invited"), "User '$email' has not been invited"
            )

        // Sync Pfadiname from Hitobito on every login
        val nickname = oauth2User.getAttribute<String>("nickname")
        if (nickname != null && nickname != user.pfadiName) {
            authorizedUserRepository.save(user.copy(pfadiName = nickname))
        }

        val authorities = user.roles.map { SimpleGrantedAuthority(it.name) }
        return DefaultOAuth2User(authorities, oauth2User.attributes, "email")
    }
}
