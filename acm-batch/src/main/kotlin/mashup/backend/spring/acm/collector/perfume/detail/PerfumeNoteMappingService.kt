package mashup.backend.spring.acm.collector.perfume.detail

import mashup.backend.spring.acm.domain.perfume.PerfumeNoteType
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired

open class PerfumeNoteMappingService {
    @Autowired
    lateinit var perfumeService: PerfumeService

    fun saveNotes(document: Document, perfumeUrl: String): SimplePerfume {
        val simplePerfume = parsePerfume(document = document, perfumeUrl = perfumeUrl)
        save(simplePerfume = simplePerfume)
        log.info("simplePerfume: $simplePerfume")
        return simplePerfume
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

    /**
     * 향수-노트 매핑 정보를 저장
     * 향수나 노트가 디비에 존재하지 않아서 실패하는 것들은 무시
     */
    private fun save(simplePerfume: SimplePerfume) {
        simplePerfume.topNotes.forEach { note ->
            addNoteToPerfume(
                perfumeUrl = simplePerfume.url,
                noteUrl = note.url,
                perfumeNoteType = PerfumeNoteType.TOP
            )
        }
        simplePerfume.middleNotes.forEach { note ->
            addNoteToPerfume(
                perfumeUrl = simplePerfume.url,
                noteUrl = note.url,
                perfumeNoteType = PerfumeNoteType.MIDDLE
            )
        }
        simplePerfume.baseNotes.forEach { note ->
            addNoteToPerfume(
                perfumeUrl = simplePerfume.url,
                noteUrl = note.url,
                perfumeNoteType = PerfumeNoteType.BASE
            )
        }
        simplePerfume.otherNotes.forEach { note ->
            addNoteToPerfume(
                perfumeUrl = simplePerfume.url,
                noteUrl = note.url,
                perfumeNoteType = PerfumeNoteType.UNKNOWN
            )
        }
    }

    private fun addNoteToPerfume(perfumeUrl: String, noteUrl: String, perfumeNoteType: PerfumeNoteType) {
        val correctedPerfumeUrl = correctUrl(perfumeUrl)
        val correctedNoteUrl = correctUrl(noteUrl)
        try {
            perfumeService.add(
                perfumeUrl = correctedPerfumeUrl,
                noteUrl = correctedNoteUrl,
                noteType = perfumeNoteType
            )
        } catch (e: Exception) {
            log.error("Failed mapping. perfumeUrl: $correctedPerfumeUrl, noteUrl: $correctedNoteUrl, type: $perfumeNoteType",
                e)
        }
    }

    private fun correctUrl(url: String): String = url.replace("--", "-")

    companion object {
        val log: Logger = LoggerFactory.getLogger(PerfumeNoteMappingService::class.java)
    }
}