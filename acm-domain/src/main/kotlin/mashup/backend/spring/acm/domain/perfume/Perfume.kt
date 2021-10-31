package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.*

@Entity
class Perfume(
    val name: String,
    val originalName: String,
    val brand: String,
    val originalBrand: String,
    @Enumerated(EnumType.STRING)
    val gender: Gender,
    var description: String = "",
    val url: String,
    var imageUrl: String = "",
    val thumbnailImageUrl: String,
    @OneToMany(mappedBy = "perfume")
    val accords: List<PerfumeAccord> = ArrayList(),
    @OneToMany(mappedBy = "perfume")
    val notes: List<PerfumeNote> = ArrayList()
) : BaseEntity() {
    companion object {
        fun from(perfumeCreateVo: PerfumeCreateVo) = Perfume(
            name = perfumeCreateVo.name,
            originalName = perfumeCreateVo.originalName,
            brand = perfumeCreateVo.brand,
            originalBrand = perfumeCreateVo.originalBrand,
            gender = perfumeCreateVo.gender,
            description = perfumeCreateVo.description,
            url = perfumeCreateVo.url,
            thumbnailImageUrl = perfumeCreateVo.thumbnailImageUrl,
            imageUrl = perfumeCreateVo.imageUrl,
        )
    }
}
