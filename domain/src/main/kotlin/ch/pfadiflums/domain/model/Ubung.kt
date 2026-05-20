package ch.pfadiflums.domain.model

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime

data class Ubung(
    val id: Long? = null,
    val stufe: Stufe,
    val date: LocalDate,
    val antretenTime: LocalTime,
    val antretenLocation: String,
    val abtretenTime: LocalTime,
    val abtretenLocation: String,
    val motto: String? = null,
    val tenue: String? = null,
    val mitnehmen: String? = null,
    val weiteres: String? = null,
    val status: UbungStatus = UbungStatus.DRAFT,
    val createdById: Long,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null
)
