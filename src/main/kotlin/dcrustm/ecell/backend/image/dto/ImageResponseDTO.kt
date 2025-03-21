package dcrustm.ecell.backend.image.dto

import java.time.LocalDateTime

data class ImageResponseDTO(

    val id: Long,
    val fileName: String,
    val caption: String,
    val creationTime: LocalDateTime,
    val imageUrl: String

)