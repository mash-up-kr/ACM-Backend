package mashup.backend.spring.acm.collector.perfume

import mashup.backend.spring.acm.domain.perfume.Gender
import mashup.backend.spring.acm.domain.perfume.PerfumeCreateVo
import mashup.backend.spring.acm.domain.perfume.PerfumeService
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
                            val lastPerfume = replacePerfumeName(frenchToEnglish(getLastPerfumeName()))
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
     * 실행중인 OS를 확인하고, OS별 webdriver 실행
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
        
        webDriver = ChromeDriver() // 크롬 드라이버 실행
        webDriver.get(url)         // 워밍업 용도로 미리 url 요청 (페이지 로딩시간이 길어서 필요)
        Thread.sleep(3000)
    }

    /**
     * 버튼을 클릭하고 700millis 멈춤
     * 클릭할 수 있으면 return true, 클릭할 수 없다면(Exception 발생) return false
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
     * 현재까지 로딩된 페이지의 마지막 향수 이름을 가져옴
     * (마지막 향수 이름으로 검색 키워드와 비교하여 더보기 버튼을 클릭할지 판별하기 위함)
     */
    private fun getLastPerfumeName(): String {
        val lastElement = webDriver.findElement(By.cssSelector("div.cell.card.fr-news-box:last-child"))
        return lastElement.findElement(By.cssSelector("div > p > a")).text
    }

    /**
     * 프랑스 문자를 영문자로 변환
     * à â æ À Â Æ ç Ç é è ë ê É È Ë Ê î ï Î Ï ô œ Ô Œ ù û ü Ù Û Ü
     */
    private fun frenchToEnglish(name: String): String {
        val stringBuilder = StringBuilder()
        for (c in name) {
            var t: String
            when (c) {
                'à', 'â', 'ã', 'á' -> t = "a"
                'æ' -> t = "ae"
                'À', 'Â' -> t = "A"
                'Æ' -> t = "AE"
                'ç' -> t = "c"
                'Ç' -> t = "C"
                'é', 'è', 'ë', 'ê' -> t = "e"
                'É', 'È', 'Ë', 'Ê' -> t = "E"
                'î', 'ï' -> t = "i"
                'Î', 'Ï' -> t = "I"
                'ô', 'ó' -> t = "o"
                'œ' -> t = "oe"
                'Ô' -> t = "O"
                'Œ' -> t = "OE"
                'ù', 'û', 'ü', 'ú' -> t = "u"
                'Ù', 'Û', 'Ü' -> t = "U"
                'ķ' -> t = "k"
                else -> t = c.toString()
            }
            stringBuilder.append(t)
        }
        return stringBuilder.toString()
    }

    /**
     * 향수 이름을 소문자로 변경하고, 특수문자 공백 처리
     */
    private fun replacePerfumeName(name: String): String {
        return name.toLowerCase()
            .replace("[#~`!@%^&Z*?\\-/+,]".toRegex(), " ")
            .replace("['’.]".toRegex(), "")
            .trim()
    }

    /**
     * 검색 키워드와 향수 이름을 비교
     * 맨 앞 두 글자끼리 비교하여 같으면 return true, 같지 않다면 return false
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
     * 웹 페이지에 있는 향수 정보를 파싱
     */
    private fun getPerfumes(keyword: String): List<Perfume> {
        return webDriver.findElements(By.cssSelector("div.cell.card.fr-news-box"))
            .filter {
                val perfumeName = it.findElement(By.cssSelector("div > p > a")).text
                isSameKeyword(keyword, replacePerfumeName(frenchToEnglish(perfumeName)))
            }.map {
                val imageTag = it.findElement(By.cssSelector("div.card-section > img"))
                val aTag = it.findElement(By.cssSelector("div > p > a"))
                val smallTag = it.findElement(By.cssSelector("div > p > small"))
                val url = aTag.getAttribute("href")
                Perfume(
                    name = frenchToEnglish(aTag.text),
                    originalName = aTag.text,
                    brand = frenchToEnglish(smallTag.text),
                    originalBrand = smallTag.text,
                    url = url,
                    thumbnailImageUrl = imageTag.getAttribute("src")
                )
            }
    }

    /**
     * 검색할 키워드를 만듦 (총 713가지 키워드)
     */
    private fun makeKeywords() {
        // 특수문자 저장 (1가지, 현재까지 '$'만 있음)
        keywords.add("$")

        // 0~9 저장 (10가지)
        for (i in 0..9) keywords.add(i.toString())

        // a' '~z' ', a~z * a~z 저장 (702가지)
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