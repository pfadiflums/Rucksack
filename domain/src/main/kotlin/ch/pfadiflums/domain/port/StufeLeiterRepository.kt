package ch.pfadiflums.domain.port

import ch.pfadiflums.domain.model.StufeLeiter

interface StufeLeiterRepository {
    fun findByStufeSlug(stufeSlug: String): List<StufeLeiter>
    fun save(stufeLeiter: StufeLeiter): StufeLeiter
    fun deleteById(id: Long)
    fun existsByUserIdAndStufeSlug(userId: Long, stufeSlug: String): Boolean
    fun findById(id: Long): StufeLeiter?
}
