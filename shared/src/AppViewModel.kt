package `in`.antinna.mgtn

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class ThemeMode {
    SYSTEM, LIGHT, DARK
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

    private val _language = MutableStateFlow(
        SettingsStorage.getString("language", "en")
    )
    val language: StateFlow<String> = _language.asStateFlow()

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.update { mode }
        SettingsStorage.putString("theme_mode", mode.name)
    }

    fun setLanguage(lang: String) {
        _language.update { lang }
        SettingsStorage.putString("language", lang)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang))
    }
}
