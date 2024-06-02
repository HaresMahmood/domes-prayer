package com.example.domes.presentation.routes.prayers.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.HourglassBottom
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.domes.R
import com.example.domes.presentation.common.composables.button.ActionSectionChip
import com.example.domes.presentation.common.composables.button.Variant
import com.example.domes.presentation.routes.Screen
import com.maxkeppeker.sheets.core.models.base.IconSource
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.S)
fun MainActionSection(
    timeLeft: Duration?,
    remainingTimePercentage: Float,
    notificationsGranted: Boolean,
    notificationsAllowed: Boolean,
    locale: String,
    navController: NavController,
    onSnooze: () -> Unit,
): List<@Composable () -> Unit> {
    return listOf(
        {
            timeLeft?.let { timeLeft ->
                val text = when {
                    timeLeft.toHoursPart() == 0 -> "${timeLeft.toMinutesPart()} mins left"
                    timeLeft.toMinutesPart() == 1 -> "${timeLeft.toHoursPart()}h 1 min left"
                    else -> "${timeLeft.toHoursPart()}h ${timeLeft.toMinutesPart()} mins left"
                }
                val icon = when {
                    remainingTimePercentage < 0.15 -> Icons.Outlined.HourglassEmpty
                    remainingTimePercentage < 0.5 -> Icons.Outlined.HourglassBottom
                    else -> Icons.Outlined.HourglassTop
                }

                ActionSectionChip(
                    text,
                    icon = IconSource(icon),
                    variant = Variant.VISUAL
                )
            }
        },
        { if (!notificationsAllowed) ActionSectionChip(
                if (!notificationsGranted) "Notifications disabled" else "Notifications off",
            icon = IconSource(Icons.Outlined.NotificationsOff), onClick = { navController.navigate(Screen.Notifications.route) } ) },
        { ActionSectionChip(locale, icon = IconSource(Icons.Outlined.PinDrop), onClick = { navController.navigate(Screen.Location.route) }) },
        { ActionSectionChip("Snooze alert", icon = IconSource(Icons.Outlined.Snooze)) },
        {
            ActionSectionChip(
                "Period",
                icon = IconSource(painterResource(R.drawable.menstrual_health_fill0_wght400_grad0_opsz24)),
                trailingIcon = IconSource(Icons.Outlined.Check),
                variant = Variant.SELECTED,
                onClick = onSnooze
            )
        }
    )
}