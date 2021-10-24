package mashup.backend.spring.acm.collector.note.detail

import mashup.backend.spring.acm.domain.note.NoteService
import mashup.backend.spring.acm.domain.note.NoteUpdateVo
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.tasklet.Tasklet
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Autowired

open class NoteDetailCollectorTasklet : Tasklet {
    @Autowired
    lateinit var noteService: NoteService

    override fun execute(contribution: StepContribution, chunkContext: ChunkContext): RepeatStatus? {
        val note = noteService.getFirstNoteToScrap()
        if (note == null) {
            log.info("There is no note to scrap.")
            return RepeatStatus.FINISHED
        }
        log.info("before : $note")
        val (noteGroupName, description) = getNoteDetails(note.url)
        val updatedNote = noteService.update(
            noteId = note.id,
            noteUpdateVo = NoteUpdateVo(
                noteGroupName = noteGroupName,
                description = description
            )
        )
        log.info("after  : $updatedNote")
        return RepeatStatus.FINISHED
    }

    private fun getNoteDetails(url: String): Pair<String, String> {
        val document = getDocument(url)
        val noteGroupName =
            document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div > div:nth-child(1) > div > div:nth-child(1) > h3 > b")
                .text()
        val description =
            document.select("#main-content > div.grid-x.grid-margin-x > div.small-12.medium-8.large-9.cell > div > div.cell.callout > p")
                .text()
                .replace("Odor profile: ", "")
        return Pair(noteGroupName, description)
    }

    private fun getDocument(url: String): Document = Jsoup.connect(url).get()

    companion object {
        val log: Logger = LoggerFactory.getLogger(NoteDetailCollectorTasklet::class.java)
    }
}