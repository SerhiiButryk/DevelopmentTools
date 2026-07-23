package com.web.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay

@Composable
fun NavigationComponent(
    destFirst: @Composable (onNavigateToSecond: () -> Unit) -> Unit,
    destSecond: @Composable (onNavigateBack: () -> Unit) -> Unit,
    destThird: @Composable (onNavigateBack: () -> Unit) -> Unit,
    backStack: MutableList<Screen> = mutableListOf(),
) {

    val onBack = { if (backStack.size > 1) backStack.removeLast() }

    NavDisplay(
        backStack = backStack,
        onBack = onBack,
        entryProvider = entryProvider {

            entry<Screen.Home> {
                destFirst(
                    // onNavigateToSecond
                    {
                        backStack.add(Screen.Results)
                    }
                )
            }

            entry<Screen.Results> {
                destSecond(
                    // onNavigateBack
                    onBack
                )
            }

            entry<Screen.Settings> {
                destThird(
                    // onNavigateBack
                    onBack
                )
            }

        }

    )

}