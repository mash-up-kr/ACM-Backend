package mashup.backend.spring.acm.infrastructure.scheduler.notegroup

import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.infrastructure.CacheType
import mashup.backend.spring.acm.infrastructure.cache.CacheService
import mashup.backend.spring.acm.infrastructure.scheduler.Scheduler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class NoteGroupScheduler(
    private val noteGroupService: NoteGroupService,
    private val cacheService: CacheService
): Scheduler {
    override fun init() {
        getPopularNoteGroup()
    }

    @Scheduled(cron = "0 0 5 * * *")
    fun getPopularNoteGroup() {
        log.info("[NOTE_GROUP_SCHEDULER] getPopularNoteGroup >> start")
        cacheService.clear(CacheType.POPULAR_NOTE_GROUP)
        noteGroupService.getPopularNoteGroup()
        log.info("[NOTE_GROUP_SCHEDULER] getPopularNoteGroup << end")
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(NoteGroupScheduler::class.java)
    }
}