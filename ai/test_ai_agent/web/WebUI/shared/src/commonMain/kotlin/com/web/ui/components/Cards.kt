package com.web.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.proto.Company

@Composable
fun InformationCard(
    modifier: Modifier = Modifier,
    company: Company = Company(),
) {
    val colors = MaterialTheme.colorScheme

    val spaceBetweenSections = 16.dp

    Card(
        modifier = Modifier.then(modifier)
            .fillMaxWidth()
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = colors.onBackground.copy(alpha = 0.08f),
                ambientColor = colors.onBackground.copy(alpha = 0.04f)
            ),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {

            val color = MaterialTheme.colorScheme.primary
            SelectionContainer {
                if (company.url.isNotEmpty()) {
                    Text(
                        text = getAnnotatedString(company.name, company.url, color),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = color,
                    )
                } else {
                    Text(
                        text = company.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = color,
                    )
                }
            }

            Spacer(modifier = Modifier.height(spaceBetweenSections))

            TextSection(imageVector = Icons.Outlined.Info, text = "")

            Spacer(modifier = Modifier.height(spaceBetweenSections))

            TextParagraph(text = company.summary)

            Spacer(modifier = Modifier.height(spaceBetweenSections))

            if (company.keywords.isNotEmpty()) {
                val toolsText = company.keywords.joinToString(prefix = "[ ", postfix = " ]") { it.name }
                TextParagraph(text = toolsText)
            }

            Spacer(modifier = Modifier.height(spaceBetweenSections))

            Row {
                DecoratedLabel {
                    TopLabel(colors = colors, text = company.contact_info, icon = Icons.Outlined.Contacts)
                }
                Spacer(modifier = Modifier.width(spaceBetweenSections))
                DecoratedLabel {
                    TopLabel(colors = colors, text = company.email, icon = Icons.Outlined.Email)
                }
            }

        }

    }
}

@Composable
private fun DecoratedLabel(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        content()
    }
}

private fun getAnnotatedString(text: String, url: String, color: Color): AnnotatedString {
    return buildAnnotatedString {
        val link = LinkAnnotation.Url(
            url = url,
            styles = TextLinkStyles(
                style = SpanStyle(
                    color = color,
                )
            )
        )
        withLink(link) {
            append(text)
        }
    }
}

@Composable
private fun TopLabel(colors: ColorScheme, text: String, icon: ImageVector) {
    Row {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(8.dp))
        SelectionContainer {
            Text(
                text = text,
                color = colors.onPrimaryContainer,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun TextParagraph(text: String) {
    SelectionContainer {
        Text(
            text = text,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun TextSection(imageVector: ImageVector, text: String) {
    SelectionContainer {
        Row {

            Icon(
                imageVector = imageVector,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = text,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Preview(name = "Job card dark", uiMode = UI_MODE_NIGHT_YES)
@Composable
fun InformationCardDarkPreview() {
    AppTheme(darkTheme = true) {
        InformationCard(
            company = Company(
                name = "Some name",
                email = "some email",
                summary = "some summary",
                contact_info = "some contact_info",
                outreach_message = "some outreach_message",
            ),
        )
    }
}

@Composable
private fun MetaTag(
    icon: ImageVector,
    text: String,
    contentColor: Color,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            color = contentColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(name = "Job card light")
@Composable
fun InformationCardLightPreview() {
    AppTheme(darkTheme = false) {
        InformationCard(
            company = Company(
                name = "Some name",
                email = "some email",
                summary = "some summary",
                contact_info = "some contact_info",
                outreach_message = "some outreach_message",
            ),
        )
    }
}