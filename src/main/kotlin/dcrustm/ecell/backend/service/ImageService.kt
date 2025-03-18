package dcrustm.ecell.backend.service

import dcrustm.ecell.backend.model.Image
import dcrustm.ecell.backend.repository.ImageRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ImageService(private val imageRepository: ImageRepository) {

    fun storeImage(file: MultipartFile): Image {
        val image = Image(
            fileName = file.originalFilename ?: "unknown",
            data = file.bytes
        )
        return imageRepository.save(image)
    }

    fun getAllImages(): List<Image> {
        return imageRepository.findAll()
    }
}