package eu.kanade.phoenix.migration.repository

import android.content.Context
import android.net.Uri
import eu.kanade.phoenix.migration.importer.BackupImporter
import eu.kanade.phoenix.migration.model.PhoenixBackup

class MigrationRepository(
    context: Context,
) {

    private val importer = BackupImporter(context)

    fun loadBackup(uri: Uri): PhoenixBackup {
        return importer.importBackup(uri)
    }
}
