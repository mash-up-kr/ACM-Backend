package mashup.backend.spring.acm.domain.note

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

/**
 * Note Groups:
 * 1. CITRUS SMELLS
 * 2. FRUITS, VEGETABLES AND NUTS
 * 3. FLOWERS
 * 4. WHITE FLOWERS
 * 5. GREENS, HERBS AND FOUGERES
 * 6. SPICES
 * 7. SWEETS AND GOURMAND SMELLS
 * 8. WOODS AND MOSSES
 * 9. RESINS AND BALSAMS
 * 10. MUSK, AMBER, ANIMALIC SMELLS
 * 11. BEVERAGES
 * 12. NATURAL AND SYNTHETIC, POPULAR AND WEIRD
 * 13. UNCATEGORIZED
 */
@Entity
class NoteGroup(
    var name: String,
    var description: String,
    var imageUrl: String,
    val originalName: String,
    val originalDescription: String,
    val originalImageUrl: String,
) : BaseEntity() {
    companion object {
        fun from(noteGroupCreateVo: NoteGroupCreateVo): NoteGroup = NoteGroup(
            name = noteGroupCreateVo.name,
            description = noteGroupCreateVo.description,
            imageUrl = noteGroupCreateVo.imageUrl,
            originalName = noteGroupCreateVo.name,
            originalDescription = noteGroupCreateVo.description,
            originalImageUrl = noteGroupCreateVo.imageUrl
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as NoteGroup

        if (name != other.name) return false
        if (imageUrl != other.imageUrl) return false
        if (originalName != other.originalName) return false
        if (originalImageUrl != other.originalImageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + originalName.hashCode()
        result = 31 * result + originalImageUrl.hashCode()
        return result
    }

    override fun toString(): String {
        return "NoteGroup(name='$name', description='${description.take(30)}', imageUrl='$imageUrl', originalName='$originalName', originalDescription='$${originalDescription.take(30)}', originalImageUrl='$originalImageUrl')"
    }
}