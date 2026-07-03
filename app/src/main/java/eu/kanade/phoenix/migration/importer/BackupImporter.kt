package eu.kanade.phoenix.migration.importer

import android.content.Context
import android.net.Uri
import eu.kanade.phoenix.migration.model.PhoenixBackup
import eu.kanade.phoenix.migration.model.PhoenixImportItem
import eu.kanade.phoenix.migration.model.PhoenixLibraryEntry
import eu.kanade.tachiyomi.data.backup.BackupDecoder

class BackupImporter(
    private val context: Context,
) {

    fun importBackup(uri: Uri): PhoenixBackup {

        val backup = BackupDecoder(context).decode(uri)

        val items = backup.backupManga.map { manga ->

            val chaptersRead = manga.chapters.count { it.read }

            PhoenixImportItem(
                preview = PhoenixLibraryEntry(
                    title = manga.title,
                    sourceId = manga.source,
                    url = manga.url,
                    thumbnailUrl = manga.thumbnailUrl,
                    author = manga.author,
                    artist = manga.artist,
                    genres = manga.genre,
                    status = manga.status,
                    chaptersRead = chaptersRead,
                    totalChapters = manga.chapters.size,
                    categories = manga.categories,
                ),
                backupManga = manga,
            )
        }
        return PhoenixBackup(
            manga = items,
            categories = backup.backupCategories,
        )

    }
}
