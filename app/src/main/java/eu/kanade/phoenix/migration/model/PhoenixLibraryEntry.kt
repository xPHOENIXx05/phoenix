package eu.kanade.phoenix.migration.model

data class PhoenixLibraryEntry(

    val title: String,
    val sourceId: Long,
    val url: String,

    val thumbnailUrl: String?,

    val author: String?,
    val artist: String?,

    val genres: List<String>,

    val status: Int,

    val chaptersRead: Int,
    val totalChapters: Int,

    val categories: List<Long>,

    // Future AniList support
    var anilistId: Int? = null,
    var matchedTitle: String? = null,
    var selected: Boolean = true,
)
