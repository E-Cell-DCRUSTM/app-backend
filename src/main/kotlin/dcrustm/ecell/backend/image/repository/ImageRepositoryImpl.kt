package dcrustm.ecell.backend.image.repository

import dcrustm.ecell.backend.image.dto.ImageResponseDTO
import dcrustm.ecell.backend.image.entity.ImageData
import dcrustm.ecell.backend.image.service.ImageService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.*

@Service
class ImageServiceImpl(

    private val imageRepository: ImageRepository,
    @Value("\${app.image.upload-dir}") private val uploadDir: String,
    @Value("\${app.image.base-url}") private val baseUrl: String

) : ImageService {

    companion object {
        private const val MAX_FILE_SIZE_BYTES = 10 * 1024 * 1024 // 10 MB
    }

    override fun saveImage(file: MultipartFile, caption: String): ImageResponseDTO {
        // Check file size constraint
        if (file.size > MAX_FILE_SIZE_BYTES) {
            throw IllegalArgumentException("File size exceeds maximum allowed 10MB.")
        }

        // Generate a unique file name using the current timestamp and a random UUID
        val timestamp = System.currentTimeMillis()
        val originalFilename = file.originalFilename ?: "image"
        val extension = if (originalFilename.contains(".")) {
            originalFilename.substringAfterLast(".")
        } else {
            ""
        }
        val newFileName = if (extension.isNotBlank()) {
            "$timestamp-${UUID.randomUUID()}.$extension"
        } else {
            "$timestamp-${UUID.randomUUID()}"
        }

        // Save the file on the configured directory
        val path = Paths.get(uploadDir, newFileName)
        Files.createDirectories(path.parent)
        file.transferTo(path.toFile())

        // Create the ImageData entity and save it to the database
        val imageData = ImageData(
            fileName = newFileName,
            caption = caption,
            creationTime = LocalDateTime.now()
        )
        val savedImage = imageRepository.save(imageData)

        // Create and return the response DTO with a public URL
        return ImageResponseDTO(
            id = savedImage.id,
            fileName = savedImage.fileName,
            caption = savedImage.caption,
            creationTime = savedImage.creationTime,
            imageUrl = "$baseUrl/${savedImage.fileName}"
        )
    }

    override fun getAllImages(after: LocalDateTime?): List<ImageResponseDTO> {
        val images = if (after != null) {
            imageRepository.findByCreationTimeAfter(after)
        } else {
            imageRepository.findAll()
        }
        return images.map { image ->
            ImageResponseDTO(
                id = image.id,
                fileName = image.fileName,
                caption = image.caption,
                creationTime = image.creationTime,
                imageUrl = "$baseUrl/${image.fileName}"
            )
        }
    }
}