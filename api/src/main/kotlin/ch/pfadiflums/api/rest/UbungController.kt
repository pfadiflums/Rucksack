package ch.pfadiflums.api.rest

import ch.pfadiflums.api.security.roles
import ch.pfadiflums.api.security.userId
import ch.pfadiflums.application.usecase.UbungRequest
import ch.pfadiflums.application.usecase.UbungUseCase
import ch.pfadiflums.domain.model.UbungStatus
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalTime

@RestController
@Tag(name = "Uebungen", description = "Scout section exercises (Uebungen)")
@SecurityRequirement(name = "bearerAuth")
class UbungController(private val ubungUseCase: UbungUseCase) {

    @PostMapping("/stufen/{slug}/uebungen")
    @PreAuthorize("hasRole('LEITER')")
    @Operation(summary = "Create a new Uebung for a Stufe")
    fun create(
        @PathVariable slug: String,
        @RequestBody request: UebungCreateRequest,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<UebungDto> {
        val ubung = ubungUseCase.create(
            stufeSlug = slug,
            request = request.toDomain(),
            currentUserId = jwt.userId(),
            currentUserRoles = jwt.roles()
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(ubung.toDto())
    }

    @PutMapping("/uebungen/{id}")
    @PreAuthorize("hasRole('LEITER')")
    @Operation(summary = "Update an existing Uebung")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: UebungCreateRequest,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<UebungDto> {
        val ubung = ubungUseCase.update(
            id = id,
            request = request.toDomain(),
            currentUserId = jwt.userId(),
            currentUserRoles = jwt.roles()
        )
        return ResponseEntity.ok(ubung.toDto())
    }

    @PatchMapping("/uebungen/{id}/status")
    @PreAuthorize("hasRole('LEITER')")
    @Operation(summary = "Update the status of a Uebung")
    fun setStatus(
        @PathVariable id: Long,
        @RequestBody request: UebungStatusRequest,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<UebungDto> {
        val ubung = ubungUseCase.setStatus(
            id = id,
            status = request.status,
            currentUserId = jwt.userId(),
            currentUserRoles = jwt.roles()
        )
        return ResponseEntity.ok(ubung.toDto())
    }

    @DeleteMapping("/uebungen/{id}")
    @PreAuthorize("hasRole('STUFENLEITER')")
    @Operation(summary = "Delete a Uebung (STUFENLEITER+ only)")
    fun delete(
        @PathVariable id: Long,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<Void> {
        ubungUseCase.delete(
            id = id,
            currentUserId = jwt.userId(),
            currentUserRoles = jwt.roles()
        )
        return ResponseEntity.noContent().build()
    }
}

data class UebungCreateRequest(
    val date: LocalDate,
    val antretenTime: LocalTime,
    val antretenLocation: String,
    val abtretenTime: LocalTime,
    val abtretenLocation: String,
    val motto: String? = null,
    val tenue: String? = null,
    val mitnehmen: String? = null,
    val weiteres: String? = null
) {
    fun toDomain() = UbungRequest(
        date = date,
        antretenTime = antretenTime,
        antretenLocation = antretenLocation,
        abtretenTime = abtretenTime,
        abtretenLocation = abtretenLocation,
        motto = motto,
        tenue = tenue,
        mitnehmen = mitnehmen,
        weiteres = weiteres
    )
}

data class UebungStatusRequest(val status: UbungStatus)
