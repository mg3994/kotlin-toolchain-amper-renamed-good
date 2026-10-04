package `in`.antinna.mgtn

import android.content.Context
import android.content.SharedPreferences

object SettingsStorage {
    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        if (prefs == null) {
            prefs = context.applicationContext.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        }
    }

    fun getString(key: String, defaultValue: String): String {
        return prefs?.getString(key, defaultValue) ?: defaultValue
    }

    fun putString(key: String, value: String) {
        prefs?.edit()?.putString(key, value)?.apply()
    }
}
