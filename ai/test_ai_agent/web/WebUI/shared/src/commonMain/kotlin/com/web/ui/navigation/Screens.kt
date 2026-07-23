package com.web.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object Home : Screen

    @Serializable
    data object Results : Screen

    @Serializable
    data object Settings : Screen
}