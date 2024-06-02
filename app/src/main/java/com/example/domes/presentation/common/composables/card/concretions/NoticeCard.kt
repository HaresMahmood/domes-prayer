package com.example.domes.presentation.common.composables.card.concretions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domes.presentation.common.composables.button.ActionSectionChip
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.common.composables.card.Variant
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.views.IconComponent

@Composable
fun NoticeCard(
    title: String,
    icon: IconSource,
    description: String? = null,
    bottomActions: List<@Composable () -> Unit> = emptyList(),
    variant: Variant = Variant.DEFAULT,
) {
    FixedCard(
        variant = variant
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = 20.dp,
                    bottom = if (bottomActions.isEmpty()) 20.dp else 0.dp
                )
                .align(Alignment.Start),
        ) {
            IconComponent(Modifier, iconSource = icon)
            Text(title, style = MaterialTheme.typography.titleLarge)
            description?.let {
                Spacer(Modifier.size(5.dp))
                Text(it, style = MaterialTheme.typography.bodySmall)
            }
        }

        if (bottomActions.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.End),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                bottomActions.forEach {
                    it()
                }
            }
        }
    }
}

@Preview
@Composable
fun NoticeCardPreview() {
    NoticeCard(
        "Notifications are disabled",
        IconSource(Icons.Outlined.NotificationsOff),
        "You can enable them in settings",
        listOf({
            ActionSectionChip("Enable")
        })
    )
}