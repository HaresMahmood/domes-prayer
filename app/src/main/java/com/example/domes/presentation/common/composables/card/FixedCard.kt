package com.example.domes.presentation.common.composables.card

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp

enum class Variant {
    DEFAULT,
    VISUAL,
    FADED,
    ERROR
}

@Composable
fun FixedCard(
    variant: Variant = Variant.DEFAULT,
    isFirstItem: Boolean = true,
    isLastItem: Boolean = true,
    hasGap: Boolean = true,
    hasTopRounding: Boolean = true,
    hasBottomRounding: Boolean = true,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit),
) {
    val colors = when (variant) {
        Variant.DEFAULT, Variant.FADED -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                1.dp
            ).copy(alpha = if (variant == Variant.FADED) 0.5f else 1f)
        )

        Variant.VISUAL -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )

        Variant.ERROR -> CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    }

    val topCornerSize by animateDpAsState(
        targetValue = if (isFirstItem && hasTopRounding) 35.dp else if (!isFirstItem && hasTopRounding) 12.dp else 0.dp
    )
    val bottomCornerSize by animateDpAsState(
        targetValue = if (isLastItem && hasBottomRounding) 35.dp else if (!isLastItem && hasBottomRounding) 12.dp else 0.dp
    )

    Column {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .animateContentSize()
                .then(modifier),
            colors = colors,
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
                content()
            }
        }

        if (!isLastItem && hasGap) {
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
fun FixedCardPreview() {
    Column(Modifier.padding(10.dp)) {
        FixedCard(
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
                    }
                )

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
            isFirstItem = true,
            isLastItem = false,
        )
        FixedCard(
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
                    }
                )

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
            isFirstItem = false,
            isLastItem = true,
            variant = Variant.VISUAL
        )
    }
}