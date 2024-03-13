package com.example.domes.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
    // Calculate the fraction of the scroll
    val scrollFraction = scrollBehavior.state.overlappedFraction
    // Determine the text size based on the scroll position
    val isExpanded = scrollBehavior.state.heightOffset == 0f
    val animatedTextSize by animateFloatAsState(targetValue = if (isExpanded) 45f else 25f)

//    val animatedTextSize by animateValueAsState(
//        targetValue = lerp(TextUnit.Unspecified, TextUnit.Unspecified, scrollFraction),
//        typeConverter = TwoWayConverter(
//            convertToVector = { AnimationVector1D(it.value) },
//            convertFromVector = { TextUnit(it.value, TextUnitType.Sp) }
//        )
//    )

    LargeTopAppBar(
        title = {
            Box(
                Modifier
                    .height(76.dp)
                    .animateContentSize()
            ) {
                AnimatedVisibility(
                    visible = scrollFraction == 0f,
                    exit = fadeOut(animationSpec = tween(durationMillis = 250))
                ) {
                    Text(
                        modifier = Modifier
                            .offset(8.dp, 8.dp)
                            .alpha(0.5f),
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
                    fontSize = animatedTextSize.sp,
                    fontFamily = FontFamily(Font(R.font.alexandira_arabic_variable)),
                    fontWeight = FontWeight.Medium,
                )
            }
        },
        colors = TopAppBarDefaults.largeTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            scrolledContainerColor = MaterialTheme.colorScheme.background,
        ),
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, backgroundColor = 0xFFF0EAE2)
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