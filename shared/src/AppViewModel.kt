package `in`.antinna.mgtn

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
}

enum class AppThemeColor(val colorName: String, val colorValue: Long) {
    FLAVOR_DEFAULT("Flavor Default", 0L),
    BLUE("Blue", 0xFF2196F3),
    GREEN("Green", 0xFF4CAF50),
    ORANGE("Orange", 0xFFFF9800),
    PURPLE("Purple", 0xFF9C27B0),
    RED("Red", 0xFFF44336),
    TEAL("Teal", 0xFF009688);

    fun getColor(flavorDefault: Color): Color {
        return if (this == FLAVOR_DEFAULT) flavorDefault else Color(colorValue)
    }

    companion object {
        fun fromName(name: String?): AppThemeColor {
            return entries.find { it.name.equals(name, ignoreCase = true) } ?: FLAVOR_DEFAULT
        }
    }
}

class AppViewModel : ViewModel() {
    private val _themeMode = MutableStateFlow(
        try {
            ThemeMode.valueOf(SettingsStorage.getString("theme_mode", ThemeMode.SYSTEM.name))
        } catch (e: Exception) {
            ThemeMode.SYSTEM
        }
    )
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _themeColor = MutableStateFlow(
        AppThemeColor.fromName(SettingsStorage.getString("theme_color", AppThemeColor.FLAVOR_DEFAULT.name))
    )
    val themeColor: StateFlow<AppThemeColor> = _themeColor.asStateFlow()

    private val _language = MutableStateFlow(
        SettingsStorage.getString("language", "en")
    )
    val language: StateFlow<String> = _language.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.update { mode }
        SettingsStorage.putString("theme_mode", mode.name)
    }

    fun setThemeColor(color: AppThemeColor) {
        _themeColor.update { color }
        SettingsStorage.putString("theme_color", color.name)
    }

    fun setLanguage(lang: String) {
        _language.update { lang }
        SettingsStorage.putString("language", lang)
        try {
            val locale = Locale.forLanguageTag(lang)
            Locale.setDefault(locale)
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang))
        } catch (e: Exception) {
            // Fallback
        }
    }
}
