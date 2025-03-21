package dcrustm.ecell.backend.image.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime

@Entity(name = "image_data")
data class ImageData(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val fileName: String,
    val caption: String,
    val creationTime: LocalDateTime

)