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
}