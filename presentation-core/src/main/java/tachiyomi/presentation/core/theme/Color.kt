package tachiyomi.presentation.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ColorScheme.active: Color
    @Composable
    get() {
        return if (isSystemInDarkTheme())
            Color(red = 33, green = 150, blue = 243)
        else
            Color(red = 25, green = 118, blue = 210)
    }
