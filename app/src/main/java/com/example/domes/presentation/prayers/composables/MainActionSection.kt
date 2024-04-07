package com.example.domes.presentation.prayers.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.HourglassBottom
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.HourglassTop
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Snooze
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.domes.R
import java.time.Duration

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun MainActionSection(
    timeLeft: Duration?,
    remainingTimePercentage: Float?,
    city: String,
    onSnooze: () -> Unit,
) {
    LazyRow(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val buttonCornerShapes = RoundedCornerShape(15)
        val buttonPaddingValues = PaddingValues(horizontal = 14.dp)

        item {
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(topStartPercent = 12, topEndPercent = 45, bottomStartPercent = 45, bottomEndPercent = 45),
                contentPadding = buttonPaddingValues,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                ),
            ) {
                timeLeft?.let { timeLeft ->
                    val text = when {
                        timeLeft.toHoursPart() == 0 -> "${timeLeft.toMinutesPart()} mins left"
                        timeLeft.toMinutesPart() == 1 -> "${timeLeft.toHoursPart()}h 1 min left"
                        else -> "${timeLeft.toHoursPart()}h ${timeLeft.toMinutesPart()} mins left"
                    }
                    val icon = when {
                        remainingTimePercentage!! < 0.15 -> Icons.Outlined.HourglassEmpty
                        remainingTimePercentage < 0.5 -> Icons.Outlined.HourglassBottom
                        else -> Icons.Outlined.HourglassTop
                    }

                    println("MAINACTION-REMAINING-TIME: ${remainingTimePercentage}")

                    Icon(icon,  "Remaining time")
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(text)
                }
            }
        }
        item {
            Button(
                onClick = { /*TODO*/ },
                shape = buttonCornerShapes,
                contentPadding = buttonPaddingValues,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
            ) {
                Icon(Icons.Outlined.LocationOn, "Location")
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = city)
            }
        }
        item {
            Button(
                onClick = { /*TODO*/ },
                shape = buttonCornerShapes,
                contentPadding = buttonPaddingValues,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
            ) {
                Icon(Icons.Outlined.Snooze, "Snooze alert")
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Snooze alert")
            }
        }
        item {
            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(100),
                contentPadding = buttonPaddingValues,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
            ) {
                Icon(painterResource(R.drawable.menstrual_health_fill0_wght400_grad0_opsz24), "Period")
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Period")
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(Icons.Outlined.Check, "Selected")
            }
        }
        item {
            FilledTonalIconButton(
                onClick = { /*TODO*/ }
            ) {
                Icon(Icons.Outlined.Settings, contentDescription = "Settings")
            }
        }
    }
}