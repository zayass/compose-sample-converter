package org.zayass.assessment.exchange.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.zayass.assessment.exchange.ui.theme.YetAnotherExchangerTheme

@Composable
fun ThemedSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    YetAnotherExchangerTheme {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.background,
            content = content,
        )
    }
}
