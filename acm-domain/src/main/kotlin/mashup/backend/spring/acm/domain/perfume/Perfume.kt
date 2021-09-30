package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.OneToMany

@Entity
class Perfume(
    val name: String,
    val brand: String,
    @Enumerated(EnumType.STRING)
    val gender: Gender,
    val description: String = "",
    val url: String,
    val thumbnailImageUrl: String,
    @OneToMany
    val accords: List<PerfumeAccord> = ArrayList(),
    @OneToMany
    val notes: List<PerfumeNote> = ArrayList()
) : BaseEntity() {
    companion object {
        fun from(perfumeCreateVo: PerfumeCreateVo) = Perfume(
            name = perfumeCreateVo.name,
            brand = perfumeCreateVo.brand,
            gender = perfumeCreateVo.gender,
            description = perfumeCreateVo.description,
            url = perfumeCreateVo.url,
            thumbnailImageUrl = perfumeCreateVo.thumbnailImageUrl
        )
    }
}
