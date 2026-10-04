package `in`.antinna.mgtn

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize multiplatform settings storage
        SettingsStorage.init(applicationContext)

        // 1. Real-time BuildMode mapping (Debug, Profile, Release)
        val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        val profileModeProp = System.getProperty("app.build.mode") ?: ""
        
        val currentBuildMode = when {
            profileModeProp.equals("profile", ignoreCase = true) -> BuildMode.PROFILE
            isDebuggable -> BuildMode.DEBUG
            else -> BuildMode.RELEASE
        }

        // 2. Real-time Flavor mapping from Android Manifest meta-data
        val flavorName = try {
            val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            ai.metaData?.getString("app.flavor") ?: "development"
        } catch (e: Exception) {
            "development"
        }

        val currentFlavor = when (flavorName.lowercase()) {
            "staging", "stg" -> Flavor.STAGING
            "production", "prod" -> Flavor.PRODUCTION
            else -> Flavor.DEVELOPMENT
        }

        // Initialize flavor config provider with platform-mapped values
        FlavorConfigProvider.init(currentFlavor, currentBuildMode)

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
