package `in`.antinna.mgtn

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import org.jetbrains.compose.resources.stringResource

import `in`.antinna.mgtn.resources.Res
import `in`.antinna.mgtn.resources.*

@Composable
@Preview
@DevelopmentEntryPoint
fun App() {
    val viewModel = remember { AppViewModel() }
    val themeMode by viewModel.themeMode.collectAsState()
    val themeColor by viewModel.themeColor.collectAsState()
    val language by viewModel.language.collectAsState()

    val flavorConfig = FlavorConfigProvider.current
    val activeFlavor = flavorConfig.flavor
    val activeBuildMode = flavorConfig.buildMode

    val darkTheme = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }

    val seedColor = themeColor.getColor(activeFlavor.defaultThemeSeedColor)
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
                HeaderSection(activeFlavor, activeBuildMode)
                
                Spacer(modifier = Modifier.height(16.dp))

                SettingsSection(
                    viewModel = viewModel,
                    themeMode = themeMode,
                    themeColor = themeColor,
                    language = language,
                    flavorSeedColor = activeFlavor.defaultThemeSeedColor
                )

                Spacer(modifier = Modifier.weight(1f))

                Button(onClick = { showContent = !showContent }) {
                    Text(stringResource(Res.string.click_me))
                }

                ContentSection(showContent, flavorConfig.baseUrl)

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun HeaderSection(activeFlavor: Flavor, activeBuildMode: BuildMode) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(Res.string.app_title),
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
}

@Composable
private fun SettingsSection(
    viewModel: AppViewModel,
    themeMode: ThemeMode,
    themeColor: AppThemeColor,
    language: String,
    flavorSeedColor: androidx.compose.ui.graphics.Color
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Theme Mode Selection
            Text(stringResource(Res.string.theme_settings), style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = themeMode == ThemeMode.SYSTEM,
                    onClick = { viewModel.setThemeMode(ThemeMode.SYSTEM) },
                    label = { Text(stringResource(Res.string.system_mode)) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = themeMode == ThemeMode.LIGHT,
                    onClick = { viewModel.setThemeMode(ThemeMode.LIGHT) },
                    label = { Text(stringResource(Res.string.light_mode)) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = themeMode == ThemeMode.DARK,
                    onClick = { viewModel.setThemeMode(ThemeMode.DARK) },
                    label = { Text(stringResource(Res.string.dark_mode)) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Theme Color Selection
            Text("Theme Color", style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppThemeColor.entries.forEach { colorOption ->
                    val optionColor = colorOption.getColor(flavorSeedColor)
                    val isSelected = themeColor == colorOption
                    Surface(
                        modifier = Modifier
                            .size(36.dp)
                            .clickable { viewModel.setThemeColor(colorOption) },
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = optionColor,
                        border = if (isSelected) BorderStroke(3.dp, MaterialTheme.colorScheme.onSurface) else null,
                        tonalElevation = if (isSelected) 8.dp else 2.dp
                    ) {}
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Language Selection
            Text(stringResource(Res.string.language_settings), style = MaterialTheme.typography.titleSmall)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val languages = listOf(
                    "en" to Res.string.english,
                    "es" to Res.string.spanish,
                    "hi" to Res.string.hindi,
                    "fr" to Res.string.french,
                    "de" to Res.string.german
                )
                languages.forEach { (langCode, stringRes) ->
                    FilterChip(
                        selected = language == langCode,
                        onClick = { viewModel.setLanguage(langCode) },
                        label = { Text(stringResource(stringRes)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ContentSection(showContent: Boolean, baseUrl: String) {
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
                text = stringResource(Res.string.greeting, greeting),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Base URL: $baseUrl",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
