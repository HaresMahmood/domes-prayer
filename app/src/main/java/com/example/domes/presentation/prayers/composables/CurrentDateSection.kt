package com.example.domes.presentation.prayers.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.NavigateBefore
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun CurrentDateSection(
    gregorianDate: String, // "August 2024"
    hijriDate: String, // "Muharram 28 1446"
    dayOfMonth: String, // "17"
    dayOfWeek: String // "MON"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 26.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // A circle with the day of the month, and the corresponding weekday name above it
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = dayOfWeek,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                )
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = dayOfMonth,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                }
            }

            Column {
                Text(
                    text = gregorianDate,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = hijriDate,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        Row {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Outlined.NavigateBefore,
                    contentDescription = "Previous month"
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Outlined.NavigateNext,
                    contentDescription = "Next month"
                )
            }
        }
    }
}