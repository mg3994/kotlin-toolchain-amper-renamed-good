package `in`.antinna.mgtn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.reload.DevelopmentEntryPoint
import org.jetbrains.compose.resources.painterResource

import `in`.antinna.mgtn.resources.Res
import `in`.antinna.mgtn.resources.compose_multiplatform

@Composable
@Preview
@DevelopmentEntryPoint
fun App() {
    val viewModel = remember { AppViewModel() }
    val themeMode by viewModel.themeMode.collectAsState()
    val language by viewModel.language.collectAsState()

    // Get active flavor configuration (Development, Staging, Production & Debug/Profile/Release)
    val flavorConfig = FlavorConfigProvider.current
    val activeFlavor = flavorConfig.flavor
    val activeBuildMode = flavorConfig.buildMode

    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    // Material 3 ColorScheme generated from the active flavor's seed color
    val seedColor = activeFlavor.defaultThemeSeedColor
    val colorScheme = if (darkTheme) {
        darkColorScheme(primary = seedColor)
    } else {
        lightColorScheme(primary = seedColor)
    }

    MaterialTheme(colorScheme = colorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            var showContent by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .safeContentPadding()
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Title & Flavor / BuildMode Badge
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = viewModel.getString("app_title"),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Badge(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ) {
                        Text(
                            text = "${activeFlavor.flavorName} • ${activeBuildMode.modeName}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Settings Card (Theme & Language)
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Theme Selection
                        Text(viewModel.getString("theme_settings"), style = MaterialTheme.typography.titleSmall)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = themeMode == ThemeMode.SYSTEM,
                                onClick = { viewModel.setThemeMode(ThemeMode.SYSTEM) },
                                label = { Text(viewModel.getString("system_mode")) },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = themeMode == ThemeMode.LIGHT,
                                onClick = { viewModel.setThemeMode(ThemeMode.LIGHT) },
                                label = { Text(viewModel.getString("light_mode")) },
                                modifier = Modifier.weight(1f)
                            )
                            FilterChip(
                                selected = themeMode == ThemeMode.DARK,
                                onClick = { viewModel.setThemeMode(ThemeMode.DARK) },
                                label = { Text(viewModel.getString("dark_mode")) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Language Selection
                        Text(viewModel.getString("language_settings"), style = MaterialTheme.typography.titleSmall)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = language == "en",
                                onClick = { viewModel.setLanguage("en") },
                                label = { Text(viewModel.getString("english")) }
                            )
                            FilterChip(
                                selected = language == "es",
                                onClick = { viewModel.setLanguage("es") },
                                label = { Text(viewModel.getString("spanish")) }
                            )
                            FilterChip(
                                selected = language == "hi",
                                onClick = { viewModel.setLanguage("hi") },
                                label = { Text(viewModel.getString("hindi")) }
                            )
                            FilterChip(
                                selected = language == "fr",
                                onClick = { viewModel.setLanguage("fr") },
                                label = { Text(viewModel.getString("french")) }
                            )
                            FilterChip(
                                selected = language == "de",
                                onClick = { viewModel.setLanguage("de") },
                                label = { Text(viewModel.getString("german")) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(onClick = { showContent = !showContent }) {
                    Text(viewModel.getString("click_me"))
                }

                AnimatedVisibility(showContent) {
                    val greeting = remember { Greeting().greet() }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.compose_multiplatform),
                            contentDescription = null,
                            modifier = Modifier.size(120.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = viewModel.getString("greeting", greeting),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Base URL: ${flavorConfig.baseUrl}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}
