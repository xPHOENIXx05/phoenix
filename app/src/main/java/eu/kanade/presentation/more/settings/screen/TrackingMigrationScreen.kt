package eu.kanade.presentation.more.settings.screen

import eu.kanade.phoenix.migration.ui.MigrationPreviewScreen
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import eu.kanade.presentation.more.settings.Preference
import tachiyomi.i18n.MR
import android.content.Context
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import eu.kanade.tachiyomi.util.system.toast
import tachiyomi.presentation.core.i18n.stringResource



object TrackingMigrationScreen : SearchableSettings {
    @ReadOnlyComposable
    @Composable

    override fun getTitleRes() = MR.strings.pref_category_tracking

    @Composable
    override fun getPreferences(): List<Preference> {
        val context = LocalContext.current
        val navigator = LocalNavigator.currentOrThrow

        val chooseBackup = rememberLauncherForActivityResult(
            object : ActivityResultContracts.GetContent() {
                override fun createIntent(context: Context, input: String): Intent {
                    val intent = super.createIntent(context, input)
                    return Intent.createChooser(
                        intent,
                        context.getString(android.R.string.untitled)
                    )
                }
            },
        ) { uri ->
            if (uri == null) {
                context.toast(MR.strings.file_null_uri_error)
                return@rememberLauncherForActivityResult
            }

            navigator.push(
                MigrationPreviewScreen(uri.toString())
            )
        }
        return listOf(

            Preference.PreferenceGroup(
                title = "Migration",
                preferenceItems = listOf(

                    Preference.PreferenceItem.TextPreference(
                        title = "Import Tachiyomi Backup",
                        subtitle = "Import your Tachiyomi library",
                        onClick = {
                            chooseBackup.launch("*/*")
                        },
                    ),

                    Preference.PreferenceItem.TextPreference(
                        title = "Import Mihon Backup",
                        subtitle = "Import your Mihon library",
                        onClick = {
                            chooseBackup.launch("*/*")
                        },
                    ),

                    Preference.PreferenceItem.TextPreference(
                        title = "Import SY Backup",
                        subtitle = "Import your SY backup",
                        onClick = {
                            chooseBackup.launch("*/*")
                        },
                    ),
                ),
            ),
        )
    }
}
