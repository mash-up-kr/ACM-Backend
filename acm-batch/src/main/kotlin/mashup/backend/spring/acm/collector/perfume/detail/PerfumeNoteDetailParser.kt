package mashup.backend.spring.acm.collector.perfume.detail

import mashup.backend.spring.acm.domain.perfume.PerfumeNoteCreateVo
import mashup.backend.spring.acm.domain.perfume.PerfumeNoteType
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class PerfumeNoteDetailParser {

    fun parse(document: Document, perfumeUrl: String): List<PerfumeNoteCreateVo> {
        val simplePerfume = parsePerfume(document = document, perfumeUrl = perfumeUrl)
        log.info("simplePerfume: $simplePerfume")
        return convert(simplePerfume = simplePerfume)
    }

    /**
     * 향수 정보 파싱
     */
    private fun parsePerfume(document: Document, perfumeUrl: String): SimplePerfume {
        val noteLists = document.select("#pyramid > div:nth-child(1) > div")[0].child(1)
        val perfumeId = noteLists.attr(":perfume_id")

        // top-middle-base 인 경우
        // children 7개이고 형식이 맞는지 확인 (?, top, notes, middle, notes, base, notes)
        return if (noteLists.childrenSize() == 7
            && noteLists.child(1).text().trim() == "Top Notes"
            && noteLists.child(3).text().trim() == "Middle Notes"
            && noteLists.child(5).text().trim() == "Base Notes"
        ) {
            SimplePerfume(
                perfumeId = perfumeId,
                url = perfumeUrl,
                topNotes = getNotes(noteLists.child(2)),
                middleNotes = getNotes(noteLists.child(4)),
                baseNotes = getNotes(noteLists.child(6))
            )
        } else if (noteLists.childrenSize() == 2) {
            // other 인 경우
            SimplePerfume(
                perfumeId = perfumeId,
                url = perfumeUrl,
                otherNotes = getNotes(noteLists.child(1))
            )
        } else {
            // 뭔지모름
            log.error("향수, 노트 매핑 데이터 생성중 알 수 없는 패턴의 향수 발견!")
            SimplePerfume(
                perfumeId = perfumeId,
                url = perfumeUrl
            )
        }
    }

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

    private fun convert(simplePerfume: SimplePerfume): List<PerfumeNoteCreateVo> {
        val topNotes = simplePerfume.topNotes.map {
            PerfumeNoteCreateVo(
                noteUrl = correctUrl(it.url),
                noteType = PerfumeNoteType.TOP
            )
        }
        val middleNotes = simplePerfume.middleNotes.map {
            PerfumeNoteCreateVo(
                noteUrl = correctUrl(it.url),
                noteType = PerfumeNoteType.MIDDLE
            )
        }
        val baseNotes = simplePerfume.baseNotes.map {
            PerfumeNoteCreateVo(
                noteUrl = correctUrl(it.url),
                noteType = PerfumeNoteType.BASE
            )
        }
        val unknownNotes = simplePerfume.otherNotes.map {
            PerfumeNoteCreateVo(
                noteUrl = correctUrl(it.url),
                noteType = PerfumeNoteType.UNKNOWN
            )
        }
        return topNotes + middleNotes + baseNotes + unknownNotes
    }

    private fun correctUrl(url: String): String = url.replace("--", "-")

    companion object {
        val log: Logger = LoggerFactory.getLogger(PerfumeNoteDetailParser::class.java)
    }
}