package `in`.antinna.mgtn

import androidx.compose.ui.graphics.Color

// --- Build Mode ---

interface BuildModeInterface {
    val modeName: String
}

enum class BuildMode : BuildModeInterface {
    DEBUG,
    PROFILE,
    RELEASE;

    override val modeName: String get() = name.lowercase()

    companion object {
        var current: BuildMode = DEBUG
            internal set
    }
}

// --- Flavor ---

interface FlavorInterface {
    val flavorName: String
    val baseUrl: String
    val defaultThemeMode: ThemeMode
    val defaultLocale: String
    val defaultThemeSeedColor: Color
}

enum class Flavor(
    override val flavorName: String,
    private val customUrl: String? = null
) : FlavorInterface {
    DEVELOPMENT("development"),
    STAGING("staging"),
    PRODUCTION("production");

    override val baseUrl: String
        get() {
            if (customUrl != null) return customUrl
            return when (this) {
                DEVELOPMENT -> "http://10.0.2.2:8080"
                STAGING -> "https://staging.api.antinna.in"
                PRODUCTION -> "https://api.antinna.in"
            }
        }

    override val defaultThemeMode: ThemeMode
        get() = when (this) {
            DEVELOPMENT -> ThemeMode.SYSTEM
            STAGING -> ThemeMode.SYSTEM
            PRODUCTION -> ThemeMode.SYSTEM
        }

    override val defaultLocale: String
        get() = "en"

    override val defaultThemeSeedColor: Color
        get() = when (this) {
            DEVELOPMENT -> Color(0xFF2196F3) // Blue
            STAGING -> Color(0xFF4CAF50)     // Green
            PRODUCTION -> Color(0xFFFF9800)  // Orange
        }

    companion object {
        var current: Flavor = DEVELOPMENT
            internal set
    }
}

// --- Flavor Config ---

class FlavorConfig<F : FlavorInterface, B : BuildModeInterface>(
    val flavor: F,
    val buildMode: B
) {
    val baseUrl: String get() = flavor.baseUrl
    val defaultThemeMode: ThemeMode get() = flavor.defaultThemeMode
    val defaultLocale: String get() = flavor.defaultLocale
    val defaultThemeSeedColor: Color get() = flavor.defaultThemeSeedColor
}

typealias AppFlavorConfig = FlavorConfig<Flavor, BuildMode>

object FlavorConfigProvider {
    val current: AppFlavorConfig
        get() = FlavorConfig(
            flavor = Flavor.current,
            buildMode = BuildMode.current
        )

    fun init(flavor: Flavor, buildMode: BuildMode) {
        Flavor.current = flavor
        BuildMode.current = buildMode
    }
}
