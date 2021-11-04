package mashup.backend.spring.acm.collector.brand.detail

import mashup.backend.spring.acm.domain.brand.BrandCreateVo
import mashup.backend.spring.acm.domain.util.Convert
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component

@Component
class BrandDetailParser {

    fun parse(url: String): BrandCreateVo {
        val document = getDocument(url = url)
        val brandName = getName(document)
        return BrandCreateVo(
            name = Convert.toEnglish(brandName),
            originalName = brandName,
            url = url,
            description = getDescription(document),
            logoImageUrl = getLogoImageUrl(document),
            countryName = getCountryName(document),
            websiteUrl = getWebsiteUrl(document),
            parentCompanyUrl = getParentCompanyUrl(document),
        )
    }

    private fun getDocument(url: String): Document = Jsoup.connect(url).get()

    private fun getName(document: Document): String =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div.grid-x.grid-margin-x > div.cell.text-center.dname > h1")
            .text()
            .replace(" perfumes and colognes", "")

    private fun getDescription(document: Document): String =
        document.select("#descAAA > p").joinToString(separator = "\n") { it.text() }

    private fun getLogoImageUrl(document: Document): String? =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div.grid-x.grid-margin-x > div.cell.small-12.medium-4 > div > div.cell.small-4.medium-12 > img")
            .attr("src")
            .ifBlank { null }

    private fun getCountryName(document: Document): String? =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div.grid-x.grid-margin-x > div.cell.small-12.medium-4 > div > div.cell.small-7.small-offset-1.medium-12 > a:nth-child(1) > b")
            .text()
            .ifBlank { null }

    private fun getWebsiteUrl(document: Document): String? =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div.grid-x.grid-margin-x > div.cell.small-12.medium-4 > div > div.cell.small-7.small-offset-1.medium-12 > a:nth-child(5)")
            .attr("href")
            .ifBlank { null }

    private fun getParentCompanyUrl(document: Document): String? =
        document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div.grid-x.grid-margin-x > div.cell.small-12.medium-4 > div > div.cell.small-7.small-offset-1.medium-12 > a:nth-child(7)")
            .attr("href")
            .ifBlank { null }
            ?.let { if (it.startsWith("https://www.fragrantica.com/")) it else "https://www.fragrantica.com$it" }
}