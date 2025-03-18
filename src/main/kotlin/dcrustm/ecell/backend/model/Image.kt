package dcrustm.ecell.backend.model

import jakarta.persistence.*

@Entity
@Table(name = "images")
data class Image(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    // Store a name or description for the image if needed
    val fileName: String,

    // Use @Lob to store the binary data in the database
    @Lob
    val data: ByteArray

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        if (id != other.id) return false
        if (fileName != other.fileName) return false
        if (!data.contentEquals(other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + fileName.hashCode()
        result = 31 * result + data.contentHashCode()
        return result
    }
}
