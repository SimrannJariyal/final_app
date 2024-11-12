package com.example.uff.ui.theme

import android.app.Activity
import android.os.Build
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

// Define the color schemes (Dark and Light)
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

// Define the UffTheme composable
@Composable
fun UffTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    // Choose the appropriate color scheme based on theme settings
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // Get the current view for updating the status bar color
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (view.context as? Activity)?.window

        // Convert the Compose Color to ARGB integer for the status bar
        val argbColor = AndroidColor.argb(
            (BlueHD.alpha * 255).toInt(),
            (BlueHD.red * 255).toInt(),
            (BlueHD.green * 255).toInt(),
            (BlueHD.blue * 255).toInt()
        )

        // Set the status bar color using the ARGB value
        window?.statusBarColor = argbColor

        // Adjust the appearance of status bar icons based on the dark/light theme
        val insetsController = WindowInsetsControllerCompat(window!!, view)
        insetsController.isAppearanceLightStatusBars = !darkTheme
    }

    // Apply the MaterialTheme with the chosen color scheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
