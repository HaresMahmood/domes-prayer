package com.example.domes.presentation.common.composables.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp

@Composable
fun ExpandableCard(
    content: @Composable (ColumnScope.(Boolean) -> Unit) = {},
    expandedContent: @Composable (ColumnScope.() -> Unit) = {},
    isEnabled: Boolean = true,
    isExpanded: Boolean = false,
    isFirstItem: Boolean = false,
    isLastItem: Boolean = false,
    isBeforeExpandedItem: Boolean = false,
    isAfterExpandedItem: Boolean = false,
    expand: (Boolean) -> Unit,
) {
    val backgroundElevation by animateDpAsState(
        targetValue = if (isExpanded) 5.dp else 1.dp
    )
    val topCornerSize by animateDpAsState(
        targetValue = if (isExpanded) 12.dp else if (isFirstItem || isAfterExpandedItem) 35.dp else 0.dp // TODO: Use material shapes
    )
    val bottomCornerSize by animateDpAsState(
        targetValue = if (isExpanded) 12.dp else if (isLastItem || isBeforeExpandedItem) 35.dp else 0.dp // TODO: Use material shapes
    )

    Column {
        AnimatedVisibility(isExpanded && !isFirstItem) {
            Spacer(Modifier.size(10.dp))
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
                .alpha(if (isEnabled) 1f else 0.5f)
                .clickable(
                    enabled = isEnabled,
                ) {
                    expand(isExpanded)
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                    backgroundElevation
                )
            ),
            shape = RoundedCornerShape(
                topStart = topCornerSize,
                topEnd = topCornerSize,
                bottomStart = bottomCornerSize,
                bottomEnd = bottomCornerSize
            )
        ) { 
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
//                    .padding(
//                        top = if (isFirstItem) 8.dp else 0.dp,
//                        start = 5.dp,
//                        end = 5.dp,
//                        bottom = if (isLastItem) 8.dp else 0.dp
//                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content(isExpanded)
                if (isExpanded) expandedContent()
                else if (!isLastItem && !isBeforeExpandedItem) Divider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth(0.88f)
                )
            }
        }

        AnimatedVisibility(isExpanded && !isLastItem) {
            Spacer(Modifier.size(10.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true,
    wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE
)
@Composable
fun Preview() {
    Column(Modifier.padding(10.dp)) {
        ExpandableCard(
            content = {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent,
                    ),
                    leadingContent = {
                        Icon(
                            Icons.Outlined.Timer,
                            "Setting icon",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    headlineContent = { Text("Time") },
                    supportingContent = {
                        Text(
                            "3 mins",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    trailingContent = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                    }
                )
            },
            expandedContent = {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent,
                    ),
                    leadingContent = {
                        Icon(
                            Icons.Outlined.Timer,
                            "Setting icon",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    headlineContent = { Text("Time") },
                    supportingContent = {
                        Text(
                            "3 mins",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                )
            },
            isExpanded = false,
            isFirstItem = true,
            isLastItem = false,
            expand = {}
        )
        ExpandableCard(
            content = {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent,
                    ),
                    leadingContent = {
                        Icon(
                            Icons.Outlined.Timer,
                            "Setting icon",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    headlineContent = { Text("Time") },
                    supportingContent = {
                        Text(
                            "3 mins",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    trailingContent = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = it)
                    }
                )
            },
            expandedContent = {
                ListItem(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = ListItemDefaults.colors(
                        containerColor = Color.Transparent,
                    ),
                    leadingContent = {
                        Icon(
                            Icons.Outlined.Timer,
                            "Setting icon",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    headlineContent = { Text("Time") },
                    supportingContent = {
                        Text(
                            "3 mins",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                )
            },
            isExpanded = false,
            isFirstItem = false,
            isLastItem = true,
            expand = {}
        )
    }
}