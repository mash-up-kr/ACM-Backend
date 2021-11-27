package mashup.backend.spring.acm.presentation.scheduler.cache

import mashup.backend.spring.acm.domain.note.NoteGroupCacheService
import mashup.backend.spring.acm.presentation.scheduler.Scheduler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class NoteGroupCacheScheduler(
    private val noteGroupCacheService: NoteGroupCacheService
): Scheduler {
    override fun preApply() {
        getPopularNoteGroup()
    }

    @Scheduled(cron = "0 0 5 * * *")
    fun getPopularNoteGroup() {
        log.info("[NOTE_GROUP_SCHEDULER] getPopularNoteGroup >> start")
        noteGroupCacheService.putCacheGetPopularNoteGroup()
        log.info("[NOTE_GROUP_SCHEDULER] getPopularNoteGroup << end")
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(NoteGroupCacheScheduler::class.java)
    }
}