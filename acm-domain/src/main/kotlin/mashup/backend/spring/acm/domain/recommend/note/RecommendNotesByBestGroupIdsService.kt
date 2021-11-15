package mashup.backend.spring.acm.domain.recommend.note

import mashup.backend.spring.acm.domain.note.Note
import mashup.backend.spring.acm.domain.note.NoteGroupService
import mashup.backend.spring.acm.domain.perfume.PerfumeService
import mashup.backend.spring.acm.domain.recommend.AbstractRecommendService
import mashup.backend.spring.acm.domain.recommend.RecommendRequestVo
import org.springframework.stereotype.Service

@Service
class RecommendNotesByDefaultService(
    private val noteGroupService: NoteGroupService,
    private val perfumeService: PerfumeService
): RecommendNotesService, AbstractRecommendService(noteGroupService, perfumeService) {
    override fun supports(recommendRequestVo: RecommendRequestVo): Boolean {
        return true
    }

    override fun getItems(recommendRequestVo: RecommendRequestVo): List<Note> {
        return DEFAULT_NOTE_GROUP_NAMES
            .shuffled().subList(0, recommendRequestVo.size)
            .mapNotNull { noteGroupService.getNoteGroupByName(it)?.notes }
            .flatten()
            .distinctBy { it.id }
    }
}

val DEFAULT_NOTE_GROUP_NAMES = listOf(
    "CIRTUS SMELLS",
    "FRUITS, VEGETABLES AND NUTS",
    "FLOWERS",
    "WHITE FLOWERS",
    "GREENS, HERBS AND FOUGERES",
    "SPICES",
    "SWEETS AND GOURMAND SMELLS",
    "WOODS AND MOSSES",
    "RESINS AND BALSAMS",
    "MUSK, AMBER, ANIMALIC SMELLS",
    "BEVERAGES",
    "NATURAL AND SYNTHETIC, POPULAR AND WEIRD",
    "UNCATEGORIZED"
)