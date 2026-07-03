package eu.kanade.phoenix.migration.ui

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import eu.kanade.phoenix.migration.importer.BackupImporter
import eu.kanade.phoenix.migration.service.MigrationImportService
import eu.kanade.presentation.util.Screen
import eu.kanade.tachiyomi.util.system.toast
import kotlinx.coroutines.launch

class MigrationPreviewScreen(
    private val uri: String,
) : Screen() {

    @Composable
    override fun Content() {

        val context = LocalContext.current

        val scope = rememberCoroutineScope()

        val importService = remember {
            MigrationImportService()
        }


        val library = remember {
            BackupImporter(context).importBackup(Uri.parse(uri))
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("Phoenix Migration")
                    },
                    actions = {
                        Button(
                            onClick = {
                                scope.launch {
                                    importService.importLibrary(backup = library)

                                    context.toast("Import completed")
                                }

                            }
                        ) {
                            Text("Import")
                        }
                    }
                )
            }
        ) { padding ->

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {

                items(library.manga) { manga ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = manga.preview.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(Modifier.height(8.dp))

                            Text("Author: ${manga.preview.author ?: "Unknown"}")
                            Text("Artist: ${manga.preview.artist ?: "Unknown"}")
                            Text("Read: ${manga.preview.chaptersRead}/${manga.preview.totalChapters}")
                            Text("Genres: ${manga.preview.genres.joinToString()}")

                        }
                    }
                }
            }
        }
    }
}
