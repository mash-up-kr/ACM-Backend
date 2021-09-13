package mashup.backend.spring.acm.collector.mapping

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class PerfumeNoteMapCollectorTasklet : Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        val perfumeUrls: List<String> = listOf(
            "https://www.fragrantica.com/perfume/Maison-Francis-Kurkdjian/Baccarat-Rouge-540-33519.html",
            "https://www.fragrantica.com/perfume/Dolce-Gabbana/The-One-for-Men-Eau-de-Parfum-31909.html",
            "https://www.fragrantica.com/perfume/Mugler/Angel-704.html",
            // 노트 구분 없음
            "https://www.fragrantica.com/perfume/Lush/Flying-Fox-62297.html"
        )
        val simplePerfumes = perfumeUrls.map { getPerfumeNoteMap(it) }
        log.info("simplePerfumes: $simplePerfumes")
        return RepeatStatus.FINISHED
    }

    /**
     * perfumeDetailUrl -> perfume's notes
     */
    private fun getPerfumeNoteMap(perfumeDetailUrl: String): SimplePerfume {
        val document = getDocument(perfumeDetailUrl)
        val noteLists = document.select("#pyramid > div:nth-child(1) > div")[0].child(1)
        val perfumeId = noteLists.attr(":perfume_id")

        // top-middle-base 인 경우
        // children 7개이고 형식이 맞는지 확인 (?, top, notes, middle, notes, base, notes)
        if (noteLists.childrenSize() == 7
            && noteLists.child(1).text().trim() == "Top Notes"
            && noteLists.child(3).text().trim() == "Middle Notes"
            && noteLists.child(5).text().trim() == "Base Notes") {

            return SimplePerfume(
                perfumeId = perfumeId,
                topNotes = getNotes(noteLists.child(2)),
                middleNotes = getNotes(noteLists.child(4)),
                baseNotes = getNotes(noteLists.child(6))
            )
        } else if (noteLists.childrenSize() == 2) {
            // other 인 경우
            return SimplePerfume(
                perfumeId = perfumeId,
                otherNotes = getNotes(noteLists.child(1))
            )
        } else {
            // 뭔지모름
            log.error("향수, 노트 매핑 데이터 생성중 알 수 없는 패턴의 향수 발견!")
            return SimplePerfume(perfumeId = perfumeId)
        }
    }

    /**
     * url 에 GET 요청을 보냄
     */
    private fun getDocument(url: String): Document = Jsoup.connect(url).get()

    /**
     * 향수를 구성하는 노트 정보를 파싱함
     */
    private fun getNotes(element: Element): List<SimpleNote> {
        return element.child(0).children().map {
            val imageTag = it.child(0).child(0)
            val noteThumbnailImageUrl = imageTag.attr("src")

            val divTag = it.child(1)
            val noteName = divTag.text()
            val noteUrl = divTag.child(0).attr("href")

            SimpleNote(
                name = noteName,
                url = noteUrl,
                thumbnailImageUrl = noteThumbnailImageUrl
            )
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }
}
