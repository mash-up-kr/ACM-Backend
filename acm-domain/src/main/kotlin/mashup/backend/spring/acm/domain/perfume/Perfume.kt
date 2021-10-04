package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.OneToMany

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
    @OneToMany
    val accords: List<PerfumeAccord> = ArrayList(),
    @OneToMany
    val notes: List<PerfumeNote> = ArrayList()
) : BaseEntity() {
    companion object {
        fun from(perfumeCreateVo: PerfumeCreateVo) = Perfume(
            name = perfumeCreateVo.name,
            originalName = perfumeCreateVo.originalName,
            brand = perfumeCreateVo.brand,
            originalBrand = perfumeCreateVo.originalBrand,
            gender = perfumeCreateVo.gender,
            url = perfumeCreateVo.url,
            thumbnailImageUrl = perfumeCreateVo.thumbnailImageUrl
        )
    }
}
