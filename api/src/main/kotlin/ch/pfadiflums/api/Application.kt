package ch.pfadiflums.api

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ComponentScan(basePackages = ["ch.pfadiflums"])
@EnableConfigurationProperties
@EnableJpaRepositories(basePackages = ["ch.pfadiflums.infrastructure.jpa"])
@EntityScan(basePackages = ["ch.pfadiflums.infrastructure.jpa"])
@EnableJpaAuditing
@OpenAPIDefinition(
    info = Info(
        title = "Rucksack API",
        version = "1.0",
        description = "Backend API für die Website der Pfadi St. Justus Flums"
    ),
    servers = [Server(url = "https://api.pfadiflums.ch/api/v1", description = "Production"), Server(url = "/api/v1", description = "Current instance")]
)
@EnableScheduling
@EnableAsync
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
