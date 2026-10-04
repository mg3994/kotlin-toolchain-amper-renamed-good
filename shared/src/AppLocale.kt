package `in`.antinna.mgtn

import org.jetbrains.compose.resources.StringResource
import `in`.antinna.mgtn.resources.Res
import `in`.antinna.mgtn.resources.*

enum class AppLocale(val code: String, val labelRes: StringResource) {
    ENGLISH("en", Res.string.english),
    SPANISH("es", Res.string.spanish),
    HINDI("hi", Res.string.hindi),
    FRENCH("fr", Res.string.french),
    GERMAN("de", Res.string.german);

    companion object {
        val supportedLocales: List<AppLocale> = entries

        fun fromCode(code: String): AppLocale {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
        }
    }
}
