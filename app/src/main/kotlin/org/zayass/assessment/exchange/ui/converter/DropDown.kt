package org.zayass.assessment.exchange.ui.converter

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import java.util.Currency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(
    values: List<Currency>,
    value: Currency,
    modifier: Modifier = Modifier,
    onValueChanged: (Currency) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
    ) {
        Row(Modifier.menuAnchor()) {
            Text(text = value.format())
            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
        }

        DropdownMenu(
            modifier = Modifier.exposedDropdownSize(),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            values.forEach { item ->
                DropdownMenuItem(
                    text = { Text(text = item.format()) },
                    onClick = {
                        onValueChanged(item)
                        expanded = false
                    },
                )
            }
        }
    }
}

private fun Currency.format() = currencyCode
