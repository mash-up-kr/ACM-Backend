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
     * 국가이름
     */
    val countryName: String? = null,
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
        countryName = brandCreateVo.countryName,
        websiteUrl = brandCreateVo.websiteUrl,
        parentCompanyUrl = brandCreateVo.parentCompanyUrl,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Brand

        if (name != other.name) return false
        if (url != other.url) return false
        if (logoImageUrl != other.logoImageUrl) return false
        if (countryName != other.countryName) return false
        if (websiteUrl != other.websiteUrl) return false
        if (parentCompanyUrl != other.parentCompanyUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + (logoImageUrl?.hashCode() ?: 0)
        result = 31 * result + (countryName?.hashCode() ?: 0)
        result = 31 * result + (websiteUrl?.hashCode() ?: 0)
        result = 31 * result + (parentCompanyUrl?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Brand(name='$name', url='$url', description='${description.take(30)}', logoImageUrl=$logoImageUrl, countryName=$countryName, websiteUrl=$websiteUrl, parentCompanyUrl=$parentCompanyUrl)"
    }
}