package mashup.backend.spring.acm.domain.brand

import mashup.backend.spring.acm.domain.BaseEntity
import javax.persistence.Entity

@Entity
class Brand(
    /**
     * 이름
     */
    val name: String,
    /**
     * url
     */
    val url: String,
    /**
     * 설명
     */
    val description: String,
    /**
     * 로고 이미지 url
     */
    val logoImageUrl: String? = null,
    /**
     * 국가
     */
    val country: String? = null,
    /**
     * 웹사이트 url
     */
    val websiteUrl: String? = null,
    /**
     * 모회사 url
     */
    val parentCompanyUrl: String? = null,
) : BaseEntity() {

    constructor(brandCreateVo: BrandCreateVo) : this(
        name = brandCreateVo.name,
        url = brandCreateVo.url,
        description = brandCreateVo.description,
        logoImageUrl = brandCreateVo.logoImageUrl,
        country = brandCreateVo.country,
        websiteUrl = brandCreateVo.websiteUrl,
        parentCompanyUrl = brandCreateVo.parentCompanyUrl,
    )
}