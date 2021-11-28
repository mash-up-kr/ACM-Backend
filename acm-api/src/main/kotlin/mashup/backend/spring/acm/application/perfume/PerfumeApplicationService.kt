package mashup.backend.spring.acm.application.perfume

import mashup.backend.spring.acm.application.ApplicationService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.perfume.PerfumeSimpleVo
import org.springframework.data.domain.Pageable

@ApplicationService
class PerfumeApplicationService(
    private val perfumeService: PerfumeService,
) {
    fun getPerfumes(
        brandId: Long?,
        noteId: Long?,
        pageable: Pageable,
    ): List<PerfumeSimpleVo> {
        // TODO: 향수 목록 조회
        return listOf(
            PerfumeSimpleVo(
                id = 206074,
                name = "Young Hearts Eau de Parfum",
                thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/m.61616.jpg",
                brandName = "Bruno Acampora"
            ),
            PerfumeSimpleVo(
                id = 239583,
                name = "Iranzol",
                thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/m.17264.jpg",
                brandName = "Bruno Acampora"
            ),
            PerfumeSimpleVo(
                id = 251069,
                name = "Pompeii Red Eau de Parfum",
                thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/m.66330.jpg",
                brandName = "Bruno Acampora"
            ),
            PerfumeSimpleVo(
                id = 321364,
                name = "Musc",
                thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/m.17260.jpg",
                brandName = "Bruno Acampora"
            ),
            PerfumeSimpleVo(
                id = 333930,
                name = "Ruby",
                thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/m.49297.jpg",
                brandName = "Bruno Acampora"
            ),
            PerfumeSimpleVo(
                id = 334166,
                name = "Prima T",
                thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/m.17263.jpg",
                brandName = "Bruno Acampora"
            ),
            PerfumeSimpleVo(
                id = 352418,
                name = "Iranzol Perfume Oil",
                thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/m.6859.jpg",
                brandName = "Bruno Acampora"
            ),
            PerfumeSimpleVo(
                id = 354796,
                name = "Read My Mind Eau de Parfum",
                thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/m.61613.jpg",
                brandName = "Bruno Acampora"
            ),
            PerfumeSimpleVo(
                id = 354853,
                name = "Relight My Fire Pure Essence",
                thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/m.56776.jpg",
                brandName = "Bruno Acampora"
            ),
            PerfumeSimpleVo(
                id = 365037,
                name = "Musc Gold Perfume Oil",
                thumbnailImageUrl = "https://fimgs.net/mdimg/perfume/m.32469.jpg",
                brandName = "Bruno Acampora"
            )
        )
    }


}