package mashup.backend.spring.acm.domain.recommend.note

import mashup.backend.spring.acm.domain.note.Note
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.recommend.RecommenderBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NoteRecommenderConfig(private val noteGroupService: NoteGroupService) {

    @Bean
    fun notesByNoteGroupIdsRecommender() = RecommenderBuilder<Note>()
        .recommendService(listOf(
            recommendNotesByNoteGroupIdsService(),
            recommendNotesByDefaultService(),
        ))
        .build()

    @Bean
    fun recommendNotesByNoteGroupIdsService() = RecommendNotesByNoteGroupIdsService(noteGroupService)

    @Bean
    fun recommendNotesByDefaultService() = RecommendNotesByDefaultService(noteGroupService)
}