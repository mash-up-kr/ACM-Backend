package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Perfume(
    val name: String,
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
            description = perfumeCreateVo.description,
            url = perfumeCreateVo.url,
            thumbnailImageUrl = perfumeCreateVo.thumbnailImageUrl
        )
    }
}
