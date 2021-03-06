package mashup.backend.spring.acm.collector.perfume

import mashup.backend.spring.acm.domain.perfume.Gender
import mashup.backend.spring.acm.domain.perfume.PerfumeCreateVo
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.util.Convert
import org.openqa.selenium.*
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired

class PerfumeCollectorTasklet : Tasklet {
    private lateinit var webDriver: WebDriver

    @Autowired
    lateinit var perfumeService: PerfumeService

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        val url = "https://www.fragrantica.com/search/"
        makeKeywords()
        try {
            executeWebDriver(url)
            genders.forEach { gender ->
                keywords.forEach { keyword ->
                    webDriver.get(url + "?query=${keyword}&spol=${gender}")
                    Thread.sleep(1500)
                    try {
                        val button = webDriver.findElement(
                            By.cssSelector("div.grid-x.grid-margin-x.grid-margin-y.text-center > div > button")
                        )
                        do {
                            val lastPerfume = replacePerfumeName(Convert.toEnglish(getLastPerfumeName()))
                            if (!isSameKeyword(keyword, lastPerfume)) break
                        } while (tryClick(button))

                        val perfumes = getPerfumes(keyword)
                        logger.info("search condition: $gender + $keyword -> perfumes.size: ${perfumes.size}")
                        val genderType = when (gender) {
                            "female" -> Gender.WOMAN
                            "male" -> Gender.MAN
                            else -> Gender.UNISEX
                        }
                        perfumes.forEach {
                            perfumeService.create(
                                PerfumeCreateVo(
                                    name = it.name,
                                    originalName = it.originalName,
                                    gender = genderType,
                                    url = it.url,
                                    thumbnailImageUrl = it.thumbnailImageUrl,
                                )
                            )
                        }
                    } catch (e: NoSuchElementException) {
                        logger.warn("search condition: $gender + $keyword -> No Such Result")
                        return@forEach
                    }
                }
            }
        } catch (e: Exception) {
            logger.error(e.stackTraceToString())
        } finally {
            webDriver.close()
        }
        return RepeatStatus.FINISHED
    }

    /**
     * ???????????? OS??? ????????????, OS??? webdriver ??????
     */
    private fun executeWebDriver(url: String) {
        val os = System.getProperty("os.name").split(" ")[0]
        var path = "acm-batch/src/main/resources/webdriver/"
        
        when (os) {
            "Windows" -> path += "windows-chromedriver.exe" // version: ChromeDriver 94.0.4606.61
            "Mac" -> path += "mac-chromedriver"             // version: ChromeDriver 93.0.4577.63
            "Linux" -> path += "linux-chromedriver"         // version: ChromeDriver 94.0.4606.61
        }
        logger.info("Running Operating System: $os / WebDriver Path: $path")
        System.setProperty("webdriver.chrome.driver", path)
        
        webDriver = ChromeDriver() // ?????? ???????????? ??????
        webDriver.get(url)         // ????????? ????????? ?????? url ?????? (????????? ??????????????? ????????? ??????)
        Thread.sleep(3000)
    }

    /**
     * ????????? ???????????? 700millis ??????
     * ????????? ??? ????????? return true, ????????? ??? ?????????(Exception ??????) return false
     */
    private fun tryClick(button: WebElement): Boolean {
        try {
            val webDriverWait = WebDriverWait(webDriver, 10)
            webDriverWait.until(ExpectedConditions.elementToBeClickable(button))
            button.click()
            Thread.sleep(700)
        } catch (e: WebDriverException) {
            return false
        }
        return true
    }

    /**
     * ???????????? ????????? ???????????? ????????? ?????? ????????? ?????????
     * (????????? ?????? ???????????? ?????? ???????????? ???????????? ????????? ????????? ???????????? ???????????? ??????)
     */
    private fun getLastPerfumeName(): String {
        val lastElement = webDriver.findElement(By.cssSelector("div.cell.card.fr-news-box:last-child"))
        return lastElement.findElement(By.cssSelector("div > p > a")).text
    }

    /**
     * ?????? ????????? ???????????? ????????????, ???????????? ?????? ??????
     */
    private fun replacePerfumeName(name: String): String {
        return name.toLowerCase()
            .replace("[#~`!@%^&Z*?\\-/+,]".toRegex(), " ")
            .replace("['???.]".toRegex(), "")
            .trim()
    }

    /**
     * ?????? ???????????? ?????? ????????? ??????
     * ??? ??? ??? ???????????? ???????????? ????????? return true, ?????? ????????? return false
     */
    private fun isSameKeyword(keyword: String, perfumeName: String): Boolean {
        val range: IntRange =
            if (perfumeName.length < keyword.length) perfumeName.indices
            else keyword.indices
        for (i in range) {
            if (keyword[i] != perfumeName[i]) return false
        }
        return true
    }

    /**
     * ??? ???????????? ?????? ?????? ????????? ??????
     */
    private fun getPerfumes(keyword: String): List<Perfume> {
        return webDriver.findElements(By.cssSelector("div.cell.card.fr-news-box"))
            .filter {
                val perfumeName = it.findElement(By.cssSelector("div > p > a")).text
                isSameKeyword(keyword, replacePerfumeName(Convert.toEnglish(perfumeName)))
            }.map {
                val imageTag = it.findElement(By.cssSelector("div.card-section > img"))
                val aTag = it.findElement(By.cssSelector("div > p > a"))
                val smallTag = it.findElement(By.cssSelector("div > p > small"))
                val url = aTag.getAttribute("href")
                Perfume(
                    name = Convert.toEnglish(aTag.text),
                    originalName = aTag.text,
                    brand = Convert.toEnglish(smallTag.text),
                    originalBrand = smallTag.text,
                    url = url,
                    thumbnailImageUrl = imageTag.getAttribute("src")
                )
            }
    }

    /**
     * ????????? ???????????? ?????? (??? 713?????? ?????????)
     */
    private fun makeKeywords() {
        // ???????????? ?????? (1??????, ???????????? '$'??? ??????)
        keywords.add("$")

        // 0~9 ?????? (10??????)
        for (i in 0..9) keywords.add(i.toString())

        // a' '~z' ', a~z * a~z ?????? (702??????)
        for (i in 'a'..'z') {
            keywords.add("$i ")
            for(j in 'a'..'z') keywords.add("$i$j")
        }
    }

    data class Perfume(
        val name: String,
        val originalName: String,
        val brand: String,
        val originalBrand: String,
        val url: String,
        val thumbnailImageUrl: String
    )

    companion object {
        val logger: Logger = LoggerFactory.getLogger(this::class.java)
        val genders = listOf("female", "unisex", "male")
        val keywords = mutableListOf<String>()
    }
}