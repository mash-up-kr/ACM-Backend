package mashup.backend.spring.acm.collector.perfume.detail

import mashup.backend.spring.acm.collector.brand.detail.BrandDetailParser
import mashup.backend.spring.acm.domain.brand.Brand
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import org.springframework.beans.factory.annotation.Autowired

open class PerfumeBrandMappingService {
    @Autowired
    lateinit var brandService: BrandService

    @Autowired
    lateinit var perfumeService: PerfumeService

    @Autowired
    lateinit var brandDetailParser: BrandDetailParser

    fun saveBrand(perfumeUrl: String, brandUrl: String): Brand {
        val brand = getOrCreateBrand(brandUrl = brandUrl)
        perfumeService.setBrand(
            perfumeUrl = perfumeUrl,
            brand = brand,
        )
        return brand
    }

    private fun getOrCreateBrand(brandUrl: String): Brand {
        val brand = brandService.findByUrl(url = brandUrl)
        if (brand != null) {
            return brand
        }
        val brandCreateVo = brandDetailParser.parse(url = brandUrl)
        return brandService.create(brandCreateVo = brandCreateVo)
    }
}