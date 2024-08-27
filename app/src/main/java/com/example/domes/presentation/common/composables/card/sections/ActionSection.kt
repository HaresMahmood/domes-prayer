package com.example.domes.presentation.common.composables.card.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domes.presentation.common.composables.layout.ReversibleRow

@Composable
fun ActionSection(
    actions: @Composable (RowScope.() -> Unit),
) {
    Column {
        Spacer(Modifier.size(10.dp))
        Divider()
        ReversibleRow(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            reverseLayout = true
        ) {
            actions()
        }
    }
}

@Preview
@Composable
fun ActionSectionPreview() {
    ActionSection {
        FilledTonalButton(
            onClick = { }
        ) {
            Icon(Icons.Outlined.DeleteForever, "Remove")
            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            Text("Remove")
        }
    }
}