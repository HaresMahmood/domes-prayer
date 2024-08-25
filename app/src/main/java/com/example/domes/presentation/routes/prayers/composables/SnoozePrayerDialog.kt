package com.example.domes.presentation.routes.prayers.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.domes.domain.model.prayer.Prayer
import com.example.domes.presentation.common.composables.dialog.DurationDialog
import com.example.domes.presentation.common.composables.dialog.DurationPreset
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
            DurationPreset("5 mins", Duration.ofMinutes(5)),
            DurationPreset("10 mins", Duration.ofMinutes(10)),
            DurationPreset("15 mins", Duration.ofMinutes(15)),
            DurationPreset("30 mins", Duration.ofMinutes(30)),
            DurationPreset("1 hr", Duration.ofMinutes(60)),
            DurationPreset("1 hr 30 mins", Duration.ofMinutes(90)),
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