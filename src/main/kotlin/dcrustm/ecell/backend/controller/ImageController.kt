package dcrustm.ecell.backend.controller

import dcrustm.ecell.backend.model.Image
import dcrustm.ecell.backend.service.ImageService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.Base64

// Data Transfer Object for the response
data class ImageResponse(
    val id: Long,
    val fileName: String,
    val base64Data: String
)

@RestController
@RequestMapping("/api/images")
class ImageController(private val imageService: ImageService) {

    // Endpoint to upload an image
    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImage(@RequestParam("file") file: MultipartFile): ResponseEntity<ImageResponse> {
        val savedImage: Image = imageService.storeImage(file)
        val encodedData = Base64.getEncoder().encodeToString(savedImage.data)
        val response = ImageResponse(savedImage.id, savedImage.fileName, encodedData)
        return ResponseEntity(response, HttpStatus.CREATED)
    }

    // Endpoint to fetch all images
    @GetMapping
    fun getAllImages(): List<ImageResponse> {
        return imageService.getAllImages().map { image ->
            ImageResponse(
                id = image.id,
                fileName = image.fileName,
                base64Data = Base64.getEncoder().encodeToString(image.data)
            )
        }
    }
}
