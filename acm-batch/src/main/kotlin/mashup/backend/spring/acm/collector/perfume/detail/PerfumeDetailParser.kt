package mashup.backend.spring.acm.collector.perfume.detail

import mashup.backend.spring.acm.collector.brand.detail.BrandDetailParser
import mashup.backend.spring.acm.domain.accord.AccordCreateVo
import mashup.backend.spring.acm.domain.accord.AccordService
import mashup.backend.spring.acm.domain.brand.Brand
import mashup.backend.spring.acm.domain.brand.BrandService
import mashup.backend.spring.acm.domain.perfume.Gender
import mashup.backend.spring.acm.domain.perfume.PerfumeAccordCreateVo
import mashup.backend.spring.acm.domain.perfume.PerfumeCreateVo
import mashup.backend.spring.acm.domain.util.Convert
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

class PerfumeDetailParser {
    @Autowired
    lateinit var accordService: AccordService

    @Autowired
    lateinit var perfumeNoteDetailParser: PerfumeNoteDetailParser

    @Autowired
    lateinit var brandService: BrandService

    @Autowired
    lateinit var brandDetailParser: BrandDetailParser

    fun parse(document: Document, perfumeUrl: String): PerfumeCreateVo {
        val name = getName(document)
        val brandName = getBrandName(document)
        // "{향수이름} {브랜드이름}" 으로 저장되어있어서, 브랜드이름 제거함
        val perfumeName = brandName?.let { name.trim().removeSuffix(it).trim() } ?: name.trim()
        val perfumeAccordCreateVoList = getAccords(document).map {
            PerfumeAccordCreateVo(
                accordId = accordService.createIfNotExists(
                    accordCreateVo = AccordCreateVo(
                        name = it.name,
                        textColor = it.textColor ?: "",
                        backgroundColor = it.backgroundColor ?: "",
                    )
                ).id,
                width = it.width,
                opacity = it.opacity
            )
        }
        val perfumeNoteCreateVoList = perfumeNoteDetailParser.parse(
            document = document,
            perfumeUrl = perfumeUrl
        )
        return PerfumeCreateVo(
            name = Convert.toEnglish(perfumeName),
            originalName = perfumeName,
            gender = getGender(document),
            url = perfumeUrl,
            thumbnailImageUrl = getThumbnailImageUrl(perfumeUrl) ?: "",
            imageUrl = getImageUrl(document),
            description = getDescription(document),
            perfumeAccordCreateVoList = perfumeAccordCreateVoList,
            perfumeNoteCreateVoList = perfumeNoteCreateVoList,
            brand = getOrCreateBrand(brandUrl = getBrandUrl(document))
        )
    }

    private fun getName(document: Document): String = document.select("#toptop > h1").textNodes()[0].text()

    private fun getBrandName(document: Document): String? =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-12.large-9.cell > div > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > p > a > span")
            .text()

    private fun getGender(document: Document): Gender =
        when (val gender = document.select("#toptop > h1")[0].child(0).text()) {
            "for women and men" -> Gender.UNISEX
            "for women" -> Gender.WOMAN
            "for men" -> Gender.MAN
            else -> {
                log.error("Failed to parse gender. gender: $gender")
                Gender.UNKNOWN
            }
        }

    private fun getThumbnailImageUrl(url: String): String? =
        PATTERN_PERFUME_URL.matchEntire(url)?.groups?.get(1)?.value?.let { "https://fimgs.net/mdimg/perfume/m.$it.jpg" }

    private fun getDescription(document: Document): String =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-12.large-9.cell > div > div:nth-child(2) > div:nth-child(5) > div > p:nth-child(1)")
            .text()

    private fun getImageUrl(document: Document): String =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-12.large-9.cell > div > div:nth-child(2) > div:nth-child(1) > div:nth-child(1) > div > div > img")
            .attr("src")

    private fun getAccords(document: Document): List<PerfumeAccordVo> =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-12.large-9.cell > div > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > div")[0].children()
            .map { element ->
                // color: #FFFFFF;background: #CC3300;opacity: 100%;width: 100%;
                val styleMap = element.child(0).attr("style")
                    .split(";")
                    .filter { it.isNotBlank() }
                    .groupBy(
                        { it.split(": ")[0] },
                        { it.split(": ")[1] },
                    ).mapValues { it.value[0] }
                PerfumeAccordVo(
                    name = element.text(),
                    textColor = styleMap["color"],
                    backgroundColor = styleMap["background"],
                    opacity = styleMap["opacity"]?.let { convertPercentToDouble(it) },
                    width = styleMap["width"]?.let { convertPercentToDouble(it) },
                )
            }

    private fun convertPercentToDouble(value: String): Double = value.replace("%", "").toDouble()

    private fun getBrandUrl(document: Document): String =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-12.large-9.cell > div > div:nth-child(2) > div:nth-child(1) > div:nth-child(2) > p > a")
            .attr("href")

    private fun getOrCreateBrand(brandUrl: String): Brand {
        val brand = brandService.findByUrl(url = brandUrl)
        if (brand != null) {
            return brand
        }
        val brandCreateVo = brandDetailParser.parse(url = brandUrl)
        return brandService.create(brandCreateVo = brandCreateVo)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(PerfumeDetailParser::class.java)
        val PATTERN_PERFUME_URL = Regex("https://www.fragrantica.com/perfume/.*/.*-(\\d+).html")
    }
}
