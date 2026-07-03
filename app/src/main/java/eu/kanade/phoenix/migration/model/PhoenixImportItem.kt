package eu.kanade.phoenix.migration.model

import eu.kanade.tachiyomi.data.backup.models.BackupManga

data class PhoenixImportItem(
    val preview: PhoenixLibraryEntry,
    val backupManga: BackupManga,
)
