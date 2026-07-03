package eu.kanade.phoenix.migration.screenmodel

import android.content.Context
import android.net.Uri
import cafe.adriel.voyager.core.model.StateScreenModel
import eu.kanade.phoenix.migration.model.PhoenixBackup
import eu.kanade.phoenix.migration.repository.MigrationRepository
import kotlinx.coroutines.flow.update

class MigrationPreviewScreenModel(
    context: Context,
) : StateScreenModel<PhoenixBackup>(
    PhoenixBackup(
        manga = emptyList(),
        categories = emptyList(),
    )
) {

    private val repository = MigrationRepository(context)

    fun load(uri: Uri) {
        mutableState.update {
            repository.loadBackup(uri)
        }
    }
}
