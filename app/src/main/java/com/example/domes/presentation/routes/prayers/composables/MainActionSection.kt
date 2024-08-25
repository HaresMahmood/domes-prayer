package com.example.domes.presentation.routes.prayers.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.HourglassBottom
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.domes.R
import com.example.domes.domain.model.location.Locale
import com.example.domes.domain.model.location.isUnknown
import com.example.domes.presentation.common.composables.button.ActionSectionChip
import com.example.domes.presentation.common.composables.button.Variant
import com.example.domes.presentation.routes.Screen
import com.example.domes.presentation.routes.prayers.UIConstants.Companion.getFormatter
import com.maxkeppeker.sheets.core.models.base.IconSource
import java.time.Duration
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.S)
fun MainActionSection(
    prayerTimeLeft: Duration?,
    remainingTimePercentage: Float,
    notificationsGranted: Boolean,
    notificationsAllowed: Boolean,
    locale: Locale,
    snoozedUntil: LocalDateTime? = null,
    navController: NavController,
    onSnooze: () -> Unit,
): List<@Composable () -> Unit> {
    return listOf(
        {
            val text = when {
                prayerTimeLeft == null -> "Loading..."
                prayerTimeLeft.toHoursPart() == 0 -> "${prayerTimeLeft.toMinutesPart()} mins left"
                prayerTimeLeft.toMinutesPart() == 1 -> "${prayerTimeLeft.toHoursPart()}h 1 min left"
                else -> "${prayerTimeLeft.toHoursPart()}h ${prayerTimeLeft.toMinutesPart()} mins left"
            }
            val icon = when {
                remainingTimePercentage < 0.15 -> Icons.Outlined.HourglassEmpty
                remainingTimePercentage < 0.5 -> Icons.Outlined.HourglassBottom
                else -> Icons.Outlined.HourglassTop
            }

            ActionSectionChip(
                text,
                icon = IconSource(icon),
                variant = Variant.VISUAL,
                placeholderText = if (prayerTimeLeft == null) "Loading..." else null,
            )
        },
        {
            if (!notificationsAllowed) ActionSectionChip(
                if (!notificationsGranted) "Notifications disabled" else "Notifications off",
                icon = IconSource(Icons.Outlined.NotificationsOff),
                onClick = { navController.navigate(Screen.Notifications.route) })
        },
        {
            ActionSectionChip(
                "${locale.city}, ${locale.countryCode}",
                icon = IconSource(Icons.Outlined.PinDrop),
                placeholderText = if (locale.isUnknown()) "Loading..." else null,
                onClick = { navController.navigate(Screen.Location.route) })
        },
        {
            ActionSectionChip(
                if (snoozedUntil == null) "Snooze alert" else "Snoozed until ${
                    snoozedUntil.format(
                        getFormatter("HH:mm a")
                    )
                }",
                variant = if (snoozedUntil == null) Variant.DEFAULT else Variant.SELECTED,
                icon = IconSource(Icons.Outlined.Snooze),
                onClick = onSnooze
            )
        },
        {
            ActionSectionChip(
                "Period",
                icon = IconSource(painterResource(R.drawable.menstrual_health_fill0_wght400_grad0_opsz24)),
                trailingIcon = IconSource(Icons.Outlined.Check),
                variant = Variant.SELECTED
            )
        }
    )
}