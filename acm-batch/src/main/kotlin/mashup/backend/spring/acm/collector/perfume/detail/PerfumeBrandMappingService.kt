package mashup.backend.spring.acm.collector.perfume.detail

import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import org.springframework.beans.factory.annotation.Autowired

open class PerfumeBrandMappingService {
    @Autowired
    lateinit var brandService: BrandService

    @Autowired
    lateinit var perfumeService: PerfumeService

    fun saveBrand(perfumeUrl: String, brandUrl: String) {
        val brand = brandService.findByUrl(url = brandUrl) ?: throw RuntimeException("브랜드 조회 실패. brandUrl: $brandUrl")
        perfumeService.setBrand(
            perfumeUrl = perfumeUrl,
            brand = brand,
        )
    }
}