package mashup.backend.spring.acm.collector.note

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

class NoteCollectorTasklet : Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        val url = "https://www.fragrantica.com/notes"
        val document = getDocument(url)
        val notes = getNotes(document)
        log.info("notes.size: ${notes.size}")
        log.info("notes.distinct.size: ${notes.map { it.name }.toSet().size}")
        log.info("notes: $notes")
        return RepeatStatus.FINISHED
    }

    private fun getDocument(url: String): Document = Jsoup.connect(url).get()

    private fun getNotes(document: Document): List<Note> {
        return document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div > div > div > div.cell.small-6.medium-4.large-3.text-center.notebox")
            .map {
                val aTag = it.child(0)
                val imageTag = it.child(0).child(0)
                try {
                    Note(
                        name = it.text().trim(),
                        url = aTag.attr("href"),
                        thumbnailImageUrl = imageTag.attr("src")
                    )
                } catch (e: Exception) {
                    log.error("Failed to parse note: ${it.text()}")
                    Note()
                }
            }
    }

    data class Note(
        val name: String = "",
        val url: String = "",
        val thumbnailImageUrl: String = ""
    )

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }
}