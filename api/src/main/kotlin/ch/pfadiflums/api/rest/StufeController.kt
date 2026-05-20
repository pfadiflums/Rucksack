package ch.pfadiflums.api.rest

import ch.pfadiflums.api.security.roles
import ch.pfadiflums.api.security.userId
import ch.pfadiflums.application.usecase.StufeLeiterUseCase
import ch.pfadiflums.domain.model.Role
import ch.pfadiflums.domain.model.Stufe
import ch.pfadiflums.domain.model.StufeLeiter
import ch.pfadiflums.domain.model.Ubung
import ch.pfadiflums.domain.port.StufeLeiterRepository
import ch.pfadiflums.domain.port.StufeRepository
import ch.pfadiflums.domain.port.UbungRepository
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
@RequestMapping("/stufen")
@Tag(name = "Stufen", description = "Scout group sections (Stufen)")
class StufeController(
    private val stufeRepository: StufeRepository,
    private val stufeLeiterRepository: StufeLeiterRepository,
    private val ubungRepository: UbungRepository,
    private val stufeLeiterUseCase: StufeLeiterUseCase
) {

    // --- Public endpoints ---

    @GetMapping
    @Operation(summary = "List all Stufen")
    fun listAll(): ResponseEntity<List<StufeOverviewDto>> =
        ResponseEntity.ok(stufeRepository.findAll().map { it.toOverviewDto() })

    @GetMapping("/{slug}")
    @Operation(summary = "Get Stufe detail including Leitungsteam and next Uebung")
    fun getBySlug(@PathVariable slug: String): ResponseEntity<StufeDetailDto> {
        val stufe = stufeRepository.findBySlug(slug)
            ?: return ResponseEntity.notFound().build()
        val leitungsteam = stufeLeiterRepository.findByStufeSlug(slug)
            .sortedBy { it.sortOrder }
            .map { it.toLeiterDto() }
        val nextUebung = ubungRepository.findNextPublished(slug, LocalDate.now())?.toDto()
        return ResponseEntity.ok(stufe.toDetailDto(leitungsteam, nextUebung))
    }

    @GetMapping("/{slug}/uebungen")
    @Operation(summary = "List published Uebungen for a Stufe")
    fun listUebungen(@PathVariable slug: String): ResponseEntity<List<UebungDto>> =
        ResponseEntity.ok(ubungRepository.findPublishedByStufeSlug(slug).map { it.toDto() })

    // --- Management endpoints ---

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Create a new Stufe (ADMIN only)")
    fun create(@RequestBody request: StufeRequest): ResponseEntity<StufeOverviewDto> {
        val stufe = stufeRepository.save(
            Stufe(
                name = request.name,
                slug = request.slug,
                tagline = request.tagline,
                description = request.description,
                primaryColor = request.primaryColor,
                groupPhotoUrl = request.groupPhotoUrl,
                googleCalendarIframeUrl = request.googleCalendarIframeUrl
            )
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(stufe.toOverviewDto())
    }

    @PutMapping("/{slug}")
    @PreAuthorize("hasRole('STUFENLEITER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Update a Stufe (STUFENLEITER+ with scope check)")
    fun update(
        @PathVariable slug: String,
        @RequestBody request: StufeRequest,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<StufeOverviewDto> {
        val existing = stufeRepository.findBySlug(slug)
            ?: return ResponseEntity.notFound().build()
        val updated = stufeRepository.save(
            existing.copy(
                name = request.name,
                slug = request.slug,
                tagline = request.tagline,
                description = request.description,
                primaryColor = request.primaryColor,
                groupPhotoUrl = request.groupPhotoUrl,
                googleCalendarIframeUrl = request.googleCalendarIframeUrl
            )
        )
        return ResponseEntity.ok(updated.toOverviewDto())
    }

    @PostMapping("/{slug}/leiter")
    @PreAuthorize("hasRole('STUFENLEITER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Assign a Leiter to a Stufe (STUFENLEITER+ with scope check)")
    fun assignLeiter(
        @PathVariable slug: String,
        @RequestBody request: AssignLeiterRequest,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<LeiterDto> {
        val stufeLeiter = stufeLeiterUseCase.assign(
            stufeSlug = slug,
            userEmail = request.email,
            sortOrder = request.sortOrder,
            currentUserId = jwt.userId(),
            currentUserRoles = jwt.roles()
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(stufeLeiter.toLeiterDto())
    }

    @DeleteMapping("/{slug}/leiter/{id}")
    @PreAuthorize("hasRole('STUFENLEITER')")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Remove a Leiter from a Stufe (STUFENLEITER+ with scope check)")
    fun removeLeiter(
        @PathVariable slug: String,
        @PathVariable id: Long,
        @AuthenticationPrincipal jwt: Jwt
    ): ResponseEntity<Void> {
        stufeLeiterUseCase.remove(
            id = id,
            stufeSlug = slug,
            currentUserId = jwt.userId(),
            currentUserRoles = jwt.roles()
        )
        return ResponseEntity.noContent().build()
    }
}

// --- DTOs ---

data class StufeOverviewDto(
    val id: Long?,
    val name: String,
    val slug: String,
    val tagline: String?,
    val primaryColor: String,
    val groupPhotoUrl: String?
)

data class StufeDetailDto(
    val id: Long?,
    val name: String,
    val slug: String,
    val tagline: String?,
    val description: String?,
    val primaryColor: String,
    val groupPhotoUrl: String?,
    val googleCalendarIframeUrl: String?,
    val leitungsteam: List<LeiterDto>,
    val nextUebung: UebungDto?
)

data class LeiterDto(
    val id: Long?,
    val pfadiName: String?,
    val email: String,
    val profilePhotoUrl: String?,
    val displayRole: String,
    val sortOrder: Int
)

data class UebungDto(
    val id: Long?,
    val stufeSlug: String,
    val date: LocalDate,
    val antretenTime: LocalTime,
    val antretenLocation: String,
    val abtretenTime: LocalTime,
    val abtretenLocation: String,
    val motto: String?,
    val tenue: String?,
    val mitnehmen: String?,
    val weiteres: String?,
    val status: String
)

data class StufeRequest(
    val name: String,
    val slug: String,
    val tagline: String? = null,
    val description: String? = null,
    val primaryColor: String,
    val groupPhotoUrl: String? = null,
    val googleCalendarIframeUrl: String? = null
)

data class AssignLeiterRequest(
    val email: String,
    val sortOrder: Int = 0
)

// --- Mapping helpers ---

private fun Stufe.toOverviewDto() = StufeOverviewDto(
    id = id,
    name = name,
    slug = slug,
    tagline = tagline,
    primaryColor = primaryColor,
    groupPhotoUrl = groupPhotoUrl
)

private fun Stufe.toDetailDto(leitungsteam: List<LeiterDto>, nextUebung: UebungDto?) = StufeDetailDto(
    id = id,
    name = name,
    slug = slug,
    tagline = tagline,
    description = description,
    primaryColor = primaryColor,
    groupPhotoUrl = groupPhotoUrl,
    googleCalendarIframeUrl = googleCalendarIframeUrl,
    leitungsteam = leitungsteam,
    nextUebung = nextUebung
)

private fun StufeLeiter.toLeiterDto(): LeiterDto {
    val displayRole = when {
        authorizedUser.roles.contains(Role.ROLE_ABTEILUNGSLEITER) -> "Abteilungsleiter"
        authorizedUser.roles.contains(Role.ROLE_STUFENLEITER) -> "Stufenleiter"
        else -> "Leiter"
    }
    return LeiterDto(
        id = id,
        pfadiName = authorizedUser.pfadiName,
        email = authorizedUser.email,
        profilePhotoUrl = authorizedUser.profilePhotoUrl,
        displayRole = displayRole,
        sortOrder = sortOrder
    )
}

fun Ubung.toDto() = UebungDto(
    id = id,
    stufeSlug = stufe.slug,
    date = date,
    antretenTime = antretenTime,
    antretenLocation = antretenLocation,
    abtretenTime = abtretenTime,
    abtretenLocation = abtretenLocation,
    motto = motto,
    tenue = tenue,
    mitnehmen = mitnehmen,
    weiteres = weiteres,
    status = status.name
)
