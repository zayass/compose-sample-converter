package org.zayass.assessment.exchange.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.zayass.assessment.exchange.R
import org.zayass.assessment.exchange.ui.theme.YetAnotherExchangerTheme

@Composable
fun Screen(
    @StringRes stringRes: Int = R.string.app_name,
    content: @Composable BoxScope.() -> Unit
) {
    YetAnotherExchangerTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { AppBar(stringRes) }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                content()
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun AppBar(@StringRes stringRes: Int) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = { Text(text = stringResource(id = stringRes)) }
    )
}

@Composable
@Preview
private fun ScreenPreview() {
    Screen {
        Text(
            text = "Hello!",
            modifier = Modifier.align(Alignment.Center)
        )
    }
}