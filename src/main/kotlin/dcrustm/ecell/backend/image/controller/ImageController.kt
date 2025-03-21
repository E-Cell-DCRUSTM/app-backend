package dcrustm.ecell.backend.image.controller

import dcrustm.ecell.backend.image.dto.ImageResponseDTO
import dcrustm.ecell.backend.image.service.ImageService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/images")
class ImageController(
    private val imageService: ImageService
) {
    // Upload image endpoint (secured; only ADMIN or SUPERUSER can access)
    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERUSER')")
    fun uploadImage(
        @RequestParam("file") file: MultipartFile,
        @RequestParam("caption") caption: String
    ): ResponseEntity<Any> {
        return try {
            val response: ImageResponseDTO = imageService.saveImage(file, caption)
            ResponseEntity.ok(response)
        } catch (e: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.message)
        } catch (e: Exception) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image")
        }
    }

    // Get all images or filter by images created after a specific timestamp
    @GetMapping
    fun getImages(
        @RequestParam(name = "after", required = false) after: String?
    ): ResponseEntity<Any> {
        val afterTime = after?.let {
            try {
                LocalDateTime.parse(it)
            } catch (ex: Exception) {
                null
            }
        }
        val images = imageService.getAllImages(afterTime)
        return ResponseEntity.ok(images)
    }
}