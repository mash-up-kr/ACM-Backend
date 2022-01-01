package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.BaseEntity
import mashup.backend.spring.acm.domain.brand.Brand
import javax.persistence.*

@Entity
@NamedEntityGraph(
    name = "perfume.brand",
    attributeNodes = [NamedAttributeNode("brand")]
)
class Perfume(
    var name: String,
    val originalName: String,
    @Enumerated(EnumType.STRING)
    val gender: Gender,
    var description: String = "",
    val url: String,
    var imageUrl: String = "",
    val thumbnailImageUrl: String,
    @OneToMany(mappedBy = "perfume")
    val accords: List<PerfumeAccord> = ArrayList(),
    @OneToMany(mappedBy = "perfume")
    val notes: List<PerfumeNote> = ArrayList(),
    @ManyToOne
    var brand: Brand? = null,
) : BaseEntity() {
    companion object {
        fun from(perfumeCreateVo: PerfumeCreateVo) = Perfume(
            name = perfumeCreateVo.name,
            originalName = perfumeCreateVo.originalName,
            gender = perfumeCreateVo.gender,
            description = perfumeCreateVo.description,
            url = perfumeCreateVo.url,
            thumbnailImageUrl = perfumeCreateVo.thumbnailImageUrl,
            imageUrl = perfumeCreateVo.imageUrl,
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Perfume

        if (name != other.name) return false
        if (originalName != other.originalName) return false
        if (gender != other.gender) return false
        if (url != other.url) return false
        if (imageUrl != other.imageUrl) return false
        if (thumbnailImageUrl != other.thumbnailImageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + originalName.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + thumbnailImageUrl.hashCode()
        return result
    }
}
