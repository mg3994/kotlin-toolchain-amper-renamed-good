package `in`.antinna.mgtn

import android.content.pm.ApplicationInfo
import android.os.Debug
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

        SettingsStorage.init(applicationContext)

        // Native framework detection for Debug, Profile, and Release modes
        val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        val isProfiling = Debug.isDebuggerConnected()

        val buildMode = when {
            isProfiling -> BuildMode.PROFILE
            isDebuggable -> BuildMode.DEBUG
            else -> BuildMode.RELEASE
        }

        FlavorConfigProvider.init(Flavor.DEVELOPMENT, buildMode)

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
