package ch.pfadiflums.domain.port

import ch.pfadiflums.domain.model.Ubung
import java.time.LocalDate

interface UbungRepository {
    fun findPublishedByStufeSlug(stufeSlug: String): List<Ubung>
    fun findNextPublished(stufeSlug: String, fromDate: LocalDate): Ubung?
    fun save(ubung: Ubung): Ubung
    fun findById(id: Long): Ubung?
    fun deleteById(id: Long)
}
