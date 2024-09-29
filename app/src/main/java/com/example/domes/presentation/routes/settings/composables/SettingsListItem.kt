package com.example.domes.presentation.routes.settings.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.LockReset
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import com.example.domes.presentation.common.composables.button.SegmentedButtonValues
import com.example.domes.presentation.common.composables.button.SegmentedButtons
import com.example.domes.presentation.common.composables.listitem.ListItemDivider
import com.example.domes.ui.theme.DomesTheme
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.views.IconComponent

enum class Variant {
    DEFAULT, ERROR
}

@Composable
fun SettingsListItem(
    title: String? = null,
    icon: IconSource,
    description: String? = null,
    value: String? = null,
    variant: Variant = Variant.DEFAULT,
    trailingContent: @Composable (() -> Unit)? = null,
    extendedContent: @Composable ((Boolean) -> Unit)? = null,
    extendedScrollingContent: @Composable ((Boolean) -> Unit)? = null,
    isEnabled: Boolean = true,
    isLastItem: Boolean = false,
    onClick: (() -> Unit)? = null,
) {
    val supportingColor = (description?.let { ListItemDefaults.contentColor.copy(alpha = 0.5f) }
        ?: value?.let { MaterialTheme.colorScheme.tertiary }) ?: ListItemDefaults.contentColor

    val colors = when (variant) {
        Variant.DEFAULT -> ListItemDefaults.colors(
            containerColor = Color.Transparent,
            leadingIconColor = MaterialTheme.colorScheme.primary,
            supportingColor = supportingColor
        )

        Variant.ERROR -> ListItemDefaults.colors(
            containerColor = Color.Transparent,
            headlineColor = MaterialTheme.colorScheme.onErrorContainer,
            leadingIconColor = MaterialTheme.colorScheme.error,
            supportingColor = supportingColor
        )
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ListItem(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 5.dp)
            .alpha(if (isEnabled) 1f else 0.5f)
            .then(if (onClick != null && isEnabled) Modifier.clickable { onClick() } else Modifier),
            colors = colors,
            headlineContent = { if (title != null) Text(title) else if (trailingContent != null) trailingContent() },
            leadingContent = {
                IconComponent(modifier = Modifier, iconSource = icon)
            },
            supportingContent = {
                if (description != null || value != null) {
                    Text(description ?: value ?: "")
                }
            },
            trailingContent = { if (title != null) trailingContent?.invoke() })

        if (extendedContent != null) {
            extendedContent(isEnabled)

            Spacer(Modifier.size(20.dp))
        }

        if (!isLastItem) ListItemDivider()
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(
    showBackground = true, wallpaper = Wallpapers.RED_DOMINATED_EXAMPLE
)
@Composable
fun DomesPreview() {
    DomesTheme {
        Column {
            SettingsListItem(title = "Location",
                description = "Auto-update, set manually",
                icon = IconSource(Icons.Outlined.LocationOn),
                extendedContent = {
                    SegmentedButtons(
                        listItemChild = true,
                        content = listOf(
                            SegmentedButtonValues(selected = true,
                                onClick = {},
                                label = { Text("System") }),
                            SegmentedButtonValues(selected = false,
                                onClick = {},
                                label = { Text("Light") }),
                            SegmentedButtonValues(selected = false,
                                onClick = {},
                                label = { Text("Dark") })
                        )
                    )
                },
                extendedScrollingContent = {

                }
            )

            SettingsListItem(
                title = "Reset",
                icon = IconSource(Icons.Outlined.LockReset),
                variant = Variant.ERROR,
                isLastItem = true
            )

            SettingsListItem(icon = IconSource(Icons.Outlined.LockReset), trailingContent = {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = "00.00000000000000000000",
                    onValueChange = {},
                    label = { Text("Latitude") },
                    textStyle = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                )
            })
        }
    }
}