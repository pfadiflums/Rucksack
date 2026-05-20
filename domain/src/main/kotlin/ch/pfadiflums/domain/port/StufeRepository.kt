package ch.pfadiflums.domain.port

import ch.pfadiflums.domain.model.Stufe

interface StufeRepository {
    fun findAll(): List<Stufe>
    fun findBySlug(slug: String): Stufe?
    fun findById(id: Long): Stufe?
    fun save(stufe: Stufe): Stufe
}
