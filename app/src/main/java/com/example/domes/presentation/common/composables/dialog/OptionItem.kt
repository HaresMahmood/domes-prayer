package com.example.domes.presentation.common.composables.dialog

import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun OptionItem(
    title: String,
    description: String,
    selected: Boolean,
    overline: String? = null
) {
    ListItem(
//        modifier = Modifier
//            .clip(MaterialTheme.shapes.extraLarge),
        overlineContent = {
            overline?.let {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        },
        headlineContent = {
            Text(
                title,
                color = if (selected) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
            )
        },
        supportingContent = {
            Text(
                description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = if (selected) MaterialTheme.colorScheme.surfaceColorAtElevation(5.dp) else Color.Transparent
        ),
        tonalElevation = 5.dp,
    )
}