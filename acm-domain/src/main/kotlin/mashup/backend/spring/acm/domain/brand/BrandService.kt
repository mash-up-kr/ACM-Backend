package mashup.backend.spring.acm.domain.brand

import mashup.backend.spring.acm.domain.exception.BrandDuplicatedException
import mashup.backend.spring.acm.domain.exception.BrandNotFoundException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface BrandService {
    fun create(brandCreateVo: BrandCreateVo): Brand
    fun rename(brandId: Long, name: String)
    fun findAll(): List<Brand>
    fun searchByName(name: String): List<BrandSimpleVo>
    fun getDetail(brandId: Long): BrandDetailVo
    fun getPopularBrands(): List<BrandSimpleVo>
    fun findByUrl(url: String): Brand?
}

@Service
@Transactional(readOnly = true)
class BrandServiceImpl(
    private val brandRepository: BrandRepository,
) : BrandService {

    @Transactional
    override fun create(brandCreateVo: BrandCreateVo): Brand {
        if (brandRepository.existsByUrl(brandCreateVo.url)) {
            throw BrandDuplicatedException(message = "이미 존재하는 브랜드입니다. url: ${brandCreateVo.url}")
        }
        return brandRepository.save(
            Brand(brandCreateVo = brandCreateVo)
        )
    }

    @Transactional
    override fun rename(brandId: Long, name: String) = brandRepository.findByIdOrNull(brandId)
        ?.run { this.rename(name) }
        ?: throw RuntimeException("브랜드를 찾을 수 없습니다. brandId: $brandId")

    override fun findAll(): List<Brand> = brandRepository.findAll()

    override fun searchByName(name: String): List<BrandSimpleVo> = brandRepository.findByNameContaining(name)
        .map { BrandSimpleVo(it) }

    override fun getDetail(brandId: Long): BrandDetailVo = brandRepository.findByIdOrNull(brandId)
        ?.let { BrandDetailVo(it) }
        ?: throw BrandNotFoundException(brandId = brandId)

    override fun getPopularBrands(): List<BrandSimpleVo> {
        return POPULAR_BRAND_URL_LIST.mapNotNull { brandRepository.findByUrl(it) }.map { BrandSimpleVo(it) }
    }

    override fun findByUrl(url: String): Brand? = brandRepository.findByUrl(url)

    companion object {
        val POPULAR_BRAND_URL_LIST: List<String> = listOf(
            // 바이레도, BYREDO
            "https://www.fragrantica.com/designers/Byredo.html",
            // 랑방, LANVIN
            "https://www.fragrantica.com/designers/Lanvin.html",
            // 조말론, Jo Malone London
            "https://www.fragrantica.com/designers/Jo-Malone-London.html",
            // 딥티크, Diptyque
            "https://www.fragrantica.com/designers/Diptyque.html",
            // 디올, DIOR
            "https://www.fragrantica.com/designers/Dior.html",
            // 샤넬, CHANEL
            "https://www.fragrantica.com/designers/Chanel.html",
            // 불가리, bulgari, BVLGARI
            "https://www.fragrantica.com/designers/Bvlgari.html",
            // 입생로랑, Yves Saint Laurent
            "https://www.fragrantica.com/designers/Yves-Saint-Laurent.html",
            // 클린, CLEAN
            "https://www.fragrantica.com/designers/Clean.html",
            // 존바바토스, John Varvatos
            "https://www.fragrantica.com/designers/John-Varvatos.html",
            // 버버리, Burberry
            "https://www.fragrantica.com/designers/Burberry.html",
        )
    }
}