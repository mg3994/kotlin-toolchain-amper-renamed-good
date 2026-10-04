package `in`.antinna.mgtn

object LocalizationManager {
    private val strings = mapOf(
        "en" to mapOf(
            "app_title" to "Modern KMP App",
            "click_me" to "Click me!",
            "greeting" to "Hello, %s!",
            "theme_settings" to "Theme Mode",
            "language_settings" to "Language",
            "system_mode" to "System",
            "light_mode" to "Light",
            "dark_mode" to "Dark",
            "english" to "English",
            "spanish" to "Español",
            "hindi" to "हिंदी",
            "french" to "Français",
            "german" to "Deutsch"
        ),
        "es" to mapOf(
            "app_title" to "Aplicación KMP Moderna",
            "click_me" to "¡Haz clic!",
            "greeting" to "¡Hola, %s!",
            "theme_settings" to "Modo de Tema",
            "language_settings" to "Idioma",
            "system_mode" to "Sistema",
            "light_mode" to "Claro",
            "dark_mode" to "Oscuro",
            "english" to "Inglés",
            "spanish" to "Español",
            "hindi" to "Hindi",
            "french" to "Francés",
            "german" to "Alemán"
        ),
        "hi" to mapOf(
            "app_title" to "आधुनिक केएमपी ऐप",
            "click_me" to "क्लिक करें!",
            "greeting" to "नमस्ते, %s!",
            "theme_settings" to "थीम मोड",
            "language_settings" to "भाषा",
            "system_mode" to "सिस्टम",
            "light_mode" to "लाइट",
            "dark_mode" to "डार्क",
            "english" to "अंग्रेज़ी (English)",
            "spanish" to "स्पैनिश (Español)",
            "hindi" to "हिंदी (Hindi)",
            "french" to "फ़्रेंच (Français)",
            "german" to "जर्मन (Deutsch)"
        ),
        "fr" to mapOf(
            "app_title" to "Application KMP Moderne",
            "click_me" to "Cliquez ici !",
            "greeting" to "Bonjour, %s !",
            "theme_settings" to "Mode Thème",
            "language_settings" to "Langue",
            "system_mode" to "Système",
            "light_mode" to "Clair",
            "dark_mode" to "Sombre",
            "english" to "Anglais (English)",
            "spanish" to "Espagnol (Español)",
            "hindi" to "Hindi (हिंदी)",
            "french" to "Français",
            "german" to "Allemand (Deutsch)"
        ),
        "de" to mapOf(
            "app_title" to "Moderne KMP App",
            "click_me" to "Klick mich!",
            "greeting" to "Hallo, %s!",
            "theme_settings" to "Design-Modus",
            "language_settings" to "Sprache",
            "system_mode" to "System",
            "light_mode" to "Hell",
            "dark_mode" to "Dunkel",
            "english" to "Englisch (English)",
            "spanish" to "Spanisch (Español)",
            "hindi" to "Hindi (हिंदी)",
            "french" to "Französisch (Français)",
            "german" to "Deutsch"
        )
    )

    fun get(lang: String, key: String, vararg args: Any): String {
        val langMap = strings[lang] ?: strings["en"] ?: emptyMap()
        val template = langMap[key] ?: strings["en"]?.get(key) ?: key
        return if (args.isNotEmpty()) {
            try {
                template.format(*args)
            } catch (e: Exception) {
                template
            }
        } else {
            template
        }
    }
}
