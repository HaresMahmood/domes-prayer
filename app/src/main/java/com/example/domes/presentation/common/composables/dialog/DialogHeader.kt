package com.example.domes.presentation.common.composables.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.domes.R
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.views.IconComponent

@Composable
fun DialogHeader(
    title: String,
    icon: IconSource? = null,
    padding: Dp = 32.dp
) {
    Column(
        modifier = Modifier.padding(top = padding, start = padding, end = padding),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        icon?.let {
            IconComponent(modifier = Modifier, iconSource = it)
        }

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily(Font(R.font.alexandira_arabic_variable)),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun DialogHeaderPreview() {
    DialogHeader(
        title = "Title",
        icon = IconSource(Icons.Outlined.Close)
    )
}