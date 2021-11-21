package mashup.backend.spring.acm.domain.perfume

import mashup.backend.spring.acm.domain.BaseEntity
import mashup.backend.spring.acm.domain.brand.Brand
import mashup.backend.spring.acm.domain.member.MemberGender
import javax.persistence.*

@Entity
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
}
