package `in`.antinna.mgtn

import androidx.compose.ui.graphics.Color

interface BuildModeInterface {
    val modeName: String
}

enum class BuildMode(override val modeName: String) : BuildModeInterface {
    DEBUG("debug"),
    PROFILE("profile"),
    RELEASE("release");

    companion object {
        var current: BuildMode = DEBUG
            internal set
    }
}

interface FlavorInterface {
    val flavorName: String
    val baseUrl: String
    val defaultThemeMode: ThemeMode
    val defaultLocale: String
    val defaultThemeSeedColor: Color
}

enum class Flavor(
    override val flavorName: String,
    override val baseUrl: String,
    override val defaultThemeSeedColor: Color
) : FlavorInterface {
    DEVELOPMENT(
        flavorName = "development",
        baseUrl = "http://10.0.2.2:8080",
        defaultThemeSeedColor = Color(0xFF2196F3) // Blue
    ),
    STAGING(
        flavorName = "staging",
        baseUrl = "https://staging.api.antinna.in",
        defaultThemeSeedColor = Color(0xFF4CAF50) // Green
    ),
    PRODUCTION(
        flavorName = "production",
        baseUrl = "https://api.antinna.in",
        defaultThemeSeedColor = Color(0xFFFF9800) // Orange
    );

    override val defaultThemeMode: ThemeMode get() = ThemeMode.SYSTEM
    override val defaultLocale: String get() = "en"

    companion object {
        var current: Flavor = DEVELOPMENT
            internal set
    }
}

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
