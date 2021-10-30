package mashup.backend.spring.acm.domain.brand

import mashup.backend.spring.acm.domain.exception.BrandDuplicatedException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface BrandService {
    fun create(brandCreateVo: BrandCreateVo): Brand
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
}