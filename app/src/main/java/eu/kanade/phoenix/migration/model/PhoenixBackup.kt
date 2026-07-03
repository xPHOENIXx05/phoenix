package eu.kanade.phoenix.migration.model

import eu.kanade.tachiyomi.data.backup.models.BackupCategory

data class PhoenixBackup(

    val manga: List<PhoenixImportItem>,

    val categories: List<BackupCategory>,
)
