package dcrustm.ecell.backend.repository

import dcrustm.ecell.backend.model.Image
import org.springframework.data.jpa.repository.JpaRepository

interface ImageRepository : JpaRepository<Image, Long>