package com.example.domes.presentation.common.composables.listitem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PinDrop
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domes.presentation.common.composables.card.FixedCard
import com.example.domes.presentation.common.composables.card.Variant
import com.maxkeppeker.sheets.core.models.base.IconSource
import com.maxkeppeker.sheets.core.views.IconComponent

data class Option(
    val title: String,
    val icon: IconSource? = null,
    val description: String? = null,
    val selected: Boolean = false,
    val disabled: Boolean = false,
)

@Composable
fun DefaultView(
    option: Option,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(10.dp),
    ) {
        option.icon?.let {
            IconComponent(modifier = Modifier, iconSource = it)
        }
        Text(option.title, style = MaterialTheme.typography.titleMedium)
    }
}


@Composable
fun OptionRow(
    options: List<Option>,
    view: @Composable (Option) -> Unit = { DefaultView(it) },
    onSelection: (index: Int, option: Option) -> Unit = { _, _ -> },
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        options.forEachIndexed { index, option ->
            item {
                FixedCard(
                    variant = if (option.selected) Variant.VISUAL else Variant.DEFAULT,
                    modifier = Modifier
                        .size(100.dp),
                    isFirstItem = option.selected,
                    isLastItem = option.selected,
                ) {
                    Box(
                        modifier = Modifier.clickable(
                            enabled = !option.disabled,
                        ) {
                            onSelection(index, option)
                        }
                    ) {
                        view(option)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun OptionRowPreview() {
    OptionRow(
        options = listOf(
            Option("Test", IconSource(Icons.Outlined.PinDrop), "Testing"),
            Option("Test", IconSource(Icons.Outlined.PinDrop), "Testing", selected = true),
        )
    )
}