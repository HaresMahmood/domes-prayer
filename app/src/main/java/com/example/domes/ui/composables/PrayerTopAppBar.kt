package com.example.domes.ui.composables

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domes.R

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PrayerTopAppBar(
    englishText: String,
    arabicText: String,
    scrollBehavior: TopAppBarScrollBehavior
) {
    // Determine the text size based on the scroll position
    val collapsedTextSize = 30
    val expandedTextSize = 45

    val englishTextSize = (collapsedTextSize + (expandedTextSize - collapsedTextSize) * (1 - scrollBehavior.state.collapsedFraction)).sp
    val arabicTextOpacity = (1 - scrollBehavior.state.collapsedFraction).coerceIn(0f, 0.5f)

    val isCollapsed = remember { derivedStateOf { scrollBehavior.state.collapsedFraction > 0.5f } }
    val isFullyCollapsed = remember { derivedStateOf { scrollBehavior.state.collapsedFraction == 1f } }

    LargeTopAppBar(
        title = {
            Box(
                contentAlignment = if (isCollapsed.value) Alignment.Center else Alignment.CenterStart
            ) {
                if (!isFullyCollapsed.value) {
                    Text(
                        modifier = Modifier
                            .offset(8.dp, 10.dp)
                            .alpha(arabicTextOpacity),
                        text = arabicText,
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        fontFamily = FontFamily(Font(R.font.alexandira_arabic_variable)),
                        fontWeight = FontWeight.Bold,
                    )
                }

                Text(
                    text = englishText,
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = englishTextSize,
                    fontFamily = FontFamily(Font(R.font.alexandira_arabic_variable)),
                    fontWeight = FontWeight.Medium,
                )
            }
        },
//        actions = {
//            FilledTonalIconButton(
//                onClick = { /*TODO*/ }
//            ) {
//                Icon(Icons.Outlined.Settings, contentDescription = "Settings")
//            }
//        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
        ),
        scrollBehavior = scrollBehavior,
    )
}


fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun LargeTopAppBarPreview() {
    // Provide a fake scrollBehavior for the preview
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = rememberTopAppBarState(),
        canScroll = { true }
    )

    MaterialTheme {
        PrayerTopAppBar("Maghrib", "مغرب", scrollBehavior = scrollBehavior)
    }
}
