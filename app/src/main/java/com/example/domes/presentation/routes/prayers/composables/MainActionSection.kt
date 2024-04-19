package com.example.domes.presentation.routes.prayers.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.HourglassBottom
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.NavController
import com.example.domes.R
import com.example.domes.presentation.common.composables.button.ActionSectionButton
import com.example.domes.presentation.common.composables.button.Variant
import com.example.domes.presentation.routes.Screen
import com.maxkeppeker.sheets.core.models.base.IconSource
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.S)
fun MainActionSection(
    timeLeft: Duration?,
    remainingTimePercentage: Float,
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

                ActionSectionButton(
                    text,
                    icon = IconSource(icon),
                    variant = Variant.VISUAL
                )
            }
        },
        { ActionSectionButton(locale, icon = IconSource(Icons.Outlined.PinDrop), onClick = { navController.navigate(Screen.Location.route) }) },
        { ActionSectionButton("Snooze alert", icon = IconSource(Icons.Outlined.Snooze)) },
        {
            ActionSectionButton(
                "Period",
                icon = IconSource(painterResource(R.drawable.menstrual_health_fill0_wght400_grad0_opsz24)),
                trailingIcon = IconSource(Icons.Outlined.Check),
                variant = Variant.SELECTED,
                onClick = onSnooze
            )
        }
    )
}