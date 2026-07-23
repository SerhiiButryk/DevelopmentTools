package com.web.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.web.ui.model.SettingsUIState

enum class SettingsTab {
    GENERAL
}

@Composable
fun SettingsScreen(
    state: SettingsUIState = SettingsUIState(systemPrompt = " Some text\n Some text\n Some text"),
    onPromptChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf(SettingsTab.GENERAL) }

    Row(
        modifier = modifier
            .fillMaxSize()

    ) {
        // --- Left Sidebar ---
        Column(
            modifier = Modifier
                .width(240.dp)
                .fillMaxHeight()
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            // Sidebar Navigation Items
            SidebarItem(
                icon = Icons.Outlined.Settings,
                label = "General",
                isSelected = selectedTab == SettingsTab.GENERAL,
                onClick = { selectedTab = SettingsTab.GENERAL },
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        // Vertical Separator Line
        HorizontalDivider(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        // --- Main Content Area ---
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(horizontal = 32.dp, vertical = 24.dp)
        ) {
            when (selectedTab) {
                SettingsTab.GENERAL -> GeneralSettingsContent(
                    state.systemPrompt,
                    onPromptChanged,
                )
            }
        }

    }
}

// Sidebar Menu Item Composable
@Composable
private fun SidebarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// General Settings View
@Composable
private fun GeneralSettingsContent(
    promptText: String,
    onPromptChanged: (String) -> Unit,
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = "General",
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(false) {
            focusRequester.requestFocus()
        }

        OutlinedTextField(
            label = {
                Text(
                    text = "System prompt text",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            value = promptText,
            shape = RoundedCornerShape(16.dp),
            onValueChange = { onPromptChanged(it) },
            textStyle = TextStyle(
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 36.sp,
                fontFamily = FontFamily.SansSerif,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 8.dp)
                .focusRequester(focusRequester),
        )

    }
}