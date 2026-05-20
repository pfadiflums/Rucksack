package ch.pfadiflums.infrastructure.adapter

import ch.pfadiflums.domain.model.AuthorizedUser
import ch.pfadiflums.domain.model.Role
import ch.pfadiflums.domain.port.AuthorizedUserRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class DatabaseInitializer(
    private val authorizedUserRepository: AuthorizedUserRepository,
    @Value("\${app.admin-email}") private val adminEmail: String
) : ApplicationRunner {

    private val log = LoggerFactory.getLogger(DatabaseInitializer::class.java)

    override fun run(args: ApplicationArguments) {
        if (authorizedUserRepository.count() == 0L) {
            authorizedUserRepository.save(
                AuthorizedUser(email = adminEmail, roles = mutableSetOf(Role.ROLE_ADMIN))
            )
            log.info("Bootstrapped initial admin account for '{}'", adminEmail)
        }
    }
}
