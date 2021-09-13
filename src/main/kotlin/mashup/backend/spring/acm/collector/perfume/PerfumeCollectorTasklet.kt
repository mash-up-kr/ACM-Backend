package mashup.backend.spring.acm.collector.perfume

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebDriverException
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class PerfumeCollectorTasklet : Tasklet {
    lateinit var webDriver: WebDriver

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        val url = "https://www.fragrantica.com/search/"
        try {
            executeWebDriver(url)
            genders.forEach {
                webDriver.get(url + "?spol=${it}")
                Thread.sleep(1500)
                val button = webDriver.findElement(
                    By.cssSelector("div.grid-x.grid-margin-x.grid-margin-y.text-center > div > button")
                )
                while (tryClick(button))
                    Thread.sleep(500)
                val perfumes = getPerfumes(it)
                log.info("perfumes.size: ${perfumes.size}")
                log.info("perfumes: $perfumes")
            }
        } catch (e: Exception) {
            log.error(e.stackTraceToString())
        } finally {
            webDriver.close()
        }
        return RepeatStatus.FINISHED
    }

    private fun executeWebDriver(url: String) {
        System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe")
        webDriver = ChromeDriver()
        webDriver.get(url)
        Thread.sleep(3000)
    }

    private fun tryClick(button: WebElement): Boolean {
        try {
            val webDriverWait = WebDriverWait(webDriver, 10)
            webDriverWait.until(ExpectedConditions.elementToBeClickable(button))
            button.click()
        } catch (e: WebDriverException) {
            return false
        }
        return true
    }

    private fun getPerfumes(gender: String): List<Perfume> {
        return webDriver.findElements(By.cssSelector("div.cell.card.fr-news-box"))
            .map {
                val imageTag = it.findElement(By.cssSelector("div.card-section > img"))
                val aTag = it.findElement(By.cssSelector("div > p > a"))
                val smallTag = it.findElement(By.cssSelector("div > p > small"))
                try {
                    val url = aTag.getAttribute("href")
                    Perfume(
                        perfumeId = url.substring(url.lastIndexOf('-') + 1, url.lastIndexOf('.')),
                        name = aTag.text,
                        brand = smallTag.text,
                        gender = gender,
                        url = url,
                        thumbnailImageUrl = imageTag.getAttribute("src")
                    )
                } catch (e: Exception) {
                    log.error("Failed to parse perfume: ${it.text.replace("\n", "-")}")
                    Perfume()
                }

            }
    }

    data class Perfume(
        val perfumeId: String = "",
        val name: String = "",
        val brand: String = "",
        val gender: String = "",
        val url: String = "",
        val thumbnailImageUrl: String = ""
    )

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
        val genders = listOf("female", "unisex", "male")
    }
}