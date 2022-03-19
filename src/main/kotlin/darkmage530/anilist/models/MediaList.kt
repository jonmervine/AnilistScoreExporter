package darkmage530.anilist.models

data class MediaList(
    val id: Int,
    val mediaId: Int,
    val status: MediaListStatus?,
    val score: Float?,
    val progress: Int?,
    val progressVolumes: Int?,
    val repeat: Int?,
    val priority: Int?,
    val private: Boolean?,
    val notes: String?,
    val hiddenFromStatusLists: Boolean?,
    val customLists: Map<String, Boolean>?,
    val advancedScores: Map<String, Float>?,
    val startedAt: FuzzyDate?,
    val completedAt: FuzzyDate?,
    val updatedAt: Int?,
    val createdAt: Int?
) {

    companion object {
        operator fun invoke() {

        }
    }
}


data class FuzzyDate(
    val year: Int?,
    val month: Int?,
    val day: Int?
) {
    companion object {
        operator fun invoke() {

        }
    }
}

enum class MediaListStatus {
    CURRENT,
    PLANNING,
    COMPLETED,
    DROPPED,
    PAUSED,
    REPEATING
}