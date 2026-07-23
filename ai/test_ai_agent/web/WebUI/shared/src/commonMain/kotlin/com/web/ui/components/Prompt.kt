package com.web.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowUpward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@Composable
fun PromptScreen(
    modifier: Modifier = Modifier,
    queryText: String = "",
    onQueryTextChanged: (String) -> Unit = {},
    onQueryEnter: () -> Unit = {},
    loading: Boolean = false,
) {

    val searchBarColor = MaterialTheme.colorScheme.surfaceContainer
    val textColor = MaterialTheme.colorScheme.onSurface
    val histTextColor = MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .sizeIn(maxWidth = 800.dp)
                .padding(horizontal = 24.dp)
        ) {

            val title: String
            if (!loading) {
                title = "What do we search today?"
            } else {
                title = "Please, wait a moment..."
            }

            // Header Text
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 32.dp),
                color = textColor,
            )

            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(all = 10.dp)
                )
            } else {
                SearchBar(
                    queryText = queryText,
                    onQueryTextChanged = onQueryTextChanged,
                    textColor = textColor,
                    histTextColor = histTextColor,
                    searchBarColor = searchBarColor,
                    onQueryEnter = onQueryEnter,
                )
            }

            Spacer(modifier = Modifier.width(4.dp))
        }
    }
}

@Composable
private fun SearchBar(
    searchBarColor: Color,
    queryText: String,
    histTextColor: Color,
    onQueryTextChanged: (String) -> Unit,
    textColor: Color,
    onQueryEnter: () -> Unit = {},
) {

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(false) {
        focusRequester.requestFocus()
    }

    // Search Bar Container
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(CircleShape)
            .background(searchBarColor)
            .padding(horizontal = 8.dp)
    ) {

        // Text Input Field
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(all = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {

            if (queryText.isEmpty()) {
                Text(
                    text = "Type anything",
                    fontSize = 16.sp,
                    color = histTextColor,
                )
            }

            BasicTextField(
                value = queryText,
                onValueChange = { onQueryTextChanged(it) },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 16.sp,
                    color = textColor,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onQueryEnter()
                    }
                )
            )
        }

        IconButton(
            onClick = onQueryEnter,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowUpward,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}