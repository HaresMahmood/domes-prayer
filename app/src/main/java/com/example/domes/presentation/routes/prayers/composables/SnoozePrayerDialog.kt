package com.example.domes.presentation.routes.prayers.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.presentation.common.composables.button.ActionSectionChip
import com.example.domes.presentation.common.composables.dialog.DurationDialog
import com.maxkeppeker.sheets.core.models.base.IconSource
import java.time.Duration

@Composable
fun SnoozePrayerDialog(
    prayer: Prayer,
    setDialogVisibility: (Boolean) -> Unit,
    onAffirmative: (duration: Duration) -> Unit,
) {
    DurationDialog(
        title = "Snooze ${prayer.name}",
        icon = IconSource(Icons.Outlined.Schedule),
        setDialogVisibility = setDialogVisibility,
        onAffirmative = onAffirmative,
        bottomActions = listOf(
            { ActionSectionChip("5 mins") },
            { ActionSectionChip("15 mins") },
            { ActionSectionChip("30 mins") },
            { ActionSectionChip("1 hr") },
            { ActionSectionChip("1 hr 30 mins") },
        )
    )
}

@Preview
@Composable
fun SnoozePrayerDialogPreview() {
    SnoozePrayerDialog(
        Prayer.FAJR,
        setDialogVisibility = {},
        onAffirmative = {},
    )
}