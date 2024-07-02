package org.zayass.assessment.exchange.ui

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

@Composable
fun Header(@StringRes stringRes: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = stringRes).uppercase(),
        modifier = modifier,
        style = MaterialTheme.typography.labelSmall,
        color = MaterialTheme.colorScheme.secondary,
    )
}