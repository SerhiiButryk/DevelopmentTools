package com.web.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.web.ui.components.AppTheme
import com.web.ui.components.InformationCard
import com.web.ui.components.PromptScreen
import com.web.ui.model.PromptUIState
import com.web.ui.model.ResultsUIState
import com.web.ui.model.SettingsUIState
import com.web.ui.model.UIViewModel
import com.web.ui.navigation.NavigationComponent
import com.web.ui.navigation.Screen

@Composable
fun App(
    darkTheme: Boolean = isSystemInDarkTheme(),
) {

    val model = viewModel<UIViewModel>(factory = UIViewModel.factory)

    val messagesState by model.messagesState.collectAsState(ResultsUIState())
    val promptState by model.promptState.collectAsState(PromptUIState())
    val settingsState by model.settingsState.collectAsState(SettingsUIState())

    val backStack = remember { mutableStateListOf<Screen>(Screen.Home) }

    var darkTheme by remember { mutableStateOf(darkTheme) }

    val onThemeChanged = { darkTheme = !darkTheme }

    NavigationComponent(
        backStack = backStack,
        destFirst = {
            AppScreen(
                backStack = backStack,
                darkTheme = darkTheme,
                onThemeChanged = onThemeChanged,
                content = { paddingValues ->
                    PromptScreen(
                        modifier = Modifier.padding(paddingValues),
                        queryText = promptState.userQuery,
                        loading = promptState.isLoading,
                        onQueryEnter = {
                            model.onTextEnter {
                                backStack.add(Screen.Results)
                            }
                        },
                        onQueryTextChanged = model::onTextChanged
                    )
                },
            )
        },
        destSecond = {
            AppScreen(
                backStack = backStack,
                darkTheme = darkTheme,
                onThemeChanged = onThemeChanged,
                content = { paddingValues ->
                    ResultsScreen(
                        modifier = Modifier.padding(paddingValues),
                        state = messagesState,
                    )
                },
            )
        },
        destThird = { onNavigateBack ->
            AppScreen(
                backStack = backStack,
                darkTheme = darkTheme,
                onThemeChanged = onThemeChanged,
                content = { paddingValues ->
                    SettingsScreen(
                        modifier = Modifier.padding(paddingValues),
                        state = settingsState,
                        onPromptChanged = model::onSystemPromptChanged,
                    )
                }
            )
        }
    )

}

@Composable
private fun AppScreen(
    darkTheme: Boolean,
    backStack: SnapshotStateList<Screen>,
    onThemeChanged: () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
) {

    AppTheme(darkTheme = darkTheme) {
        Scaffold(
            topBar = {
                CommonBar(
                    title = "AI search",
                    showSettings = !backStack.contains(Screen.Settings),
                    canNavigateBack = backStack.size > 1,
                    onBackClick = { backStack.removeLast() },
                    onToggleTheme = onThemeChanged,
                    onSettings = {
                        backStack.add(Screen.Settings)
                    }
                )
            }
        ) { paddings ->
            content(paddings)
        }
    }
}


@Composable
private fun CommonBar(
    title: String,
    canNavigateBack: Boolean,
    onBackClick: () -> Unit,
    onToggleTheme: () -> Unit,
    onSettings: () -> Unit,
    showSettings: Boolean,
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Navigate back"
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = onToggleTheme) {
                Icon(
                    imageVector = if (!isSystemInDarkTheme()) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = null
                )
            }
            if (showSettings) {
                IconButton(onClick = onSettings) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = null
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Preview
@Composable
private fun ResultsScreen(
    state: ResultsUIState = ResultsUIState(),
    modifier: Modifier = Modifier,
) {

    LazyColumn(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        val modifier = Modifier.padding(16.dp)

        if (state.hasError) {
            item {
                Text(
                    text = "Sorry, an error has occurred or no data available. Please try again later.",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        } else {
            for (company in state.companies) {
                item {
                    InformationCard(
                        modifier = modifier,
                        company = company,
                    )
                }
            }
        }

    }
}