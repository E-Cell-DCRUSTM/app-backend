package dcrustm.ecell.backend.image.repository

import dcrustm.ecell.backend.image.entity.ImageData
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ImageRepository : JpaRepository<ImageData, Long> {

    fun findByCreationTimeAfter(creationTime: LocalDateTime): List<ImageData>

}