package mashup.backend.spring.acm.collector.notegroup

import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus

open class NoteGroupCollectorTasklet : Tasklet {
    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus {
        getNoteGroups()
        return RepeatStatus.FINISHED
    }

    private fun getNoteGroups(): List<SimpleNoteGroups> {
        val cssSelector = "#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div > div.cell.text-center.gone4empty > p > a"
        val document = Jsoup.connect("https://www.fragrantica.com/notes/").get()
        val simpleNoteGroups: List<SimpleNoteGroups> = document.select(cssSelector)
            .map {
                val documentId = it.attr("href")
                val imageUrl = document.select(documentId).attr("style")
                    .let { style -> "^background: url\\('(.*)'\\);.*$".toRegex().find(style)?.groupValues?.get(1) }
                    ?: ""
                val descriptionId = documentId.replace("groupnotes", "descnotes") + " > p"
                val description = document.select(descriptionId).map { it.text() }
                    .toList()
                    .joinToString(separator = "\n")
                SimpleNoteGroups(
                    documentId = documentId,
                    name = it.text(),
                    imageUrl = imageUrl,
                    description = description
                )
            }
            .toList()
        log.info("simpleNoteGroups: $simpleNoteGroups")
        return simpleNoteGroups
    }

    data class SimpleNoteGroups(
        val documentId: String,
        val name: String,
        val imageUrl: String,
        val description: String,
    )

    companion object {
        val log: Logger = LoggerFactory.getLogger(NoteGroupCollectorTasklet::class.java)
    }
}