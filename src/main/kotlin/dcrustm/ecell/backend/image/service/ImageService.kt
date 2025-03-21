package dcrustm.ecell.backend.image.service

import dcrustm.ecell.backend.image.dto.ImageResponseDTO
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

interface ImageService {

    fun saveImage(file: MultipartFile, caption: String): ImageResponseDTO
    fun getAllImages(after: LocalDateTime? = null): List<ImageResponseDTO>

}