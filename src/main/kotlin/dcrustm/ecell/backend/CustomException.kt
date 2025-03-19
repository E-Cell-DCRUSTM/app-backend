package dcrustm.ecell.backend

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

class DuplicateEntityException(message: String) : RuntimeException(message)

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateEntityException::class)
    fun handleDuplicateEntityException(ex: DuplicateEntityException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.message)
    }

}