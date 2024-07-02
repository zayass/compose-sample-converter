package org.zayass.assessment.exchange.ui.mainscreen

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.zayass.assessment.exchange.ui.Screen
import org.zayass.assessment.exchange.ui.balances.Balances
import org.zayass.assessment.exchange.ui.converter.Converter
import org.zayass.assessment.exchange.ui.theme.dimens

@Composable
fun MainScreen() {
    Screen {
        BoxWithConstraints {
            val pageSize = maxHeight

            Column(
                modifier = Modifier
                    .padding(vertical = MaterialTheme.dimens.medium)
                    .imePadding()
                    .height(pageSize)
                    .verticalScroll(rememberScrollState())
                    .fillMaxHeight()
            ) {
                Balances()
                Spacer(modifier = Modifier.size(MaterialTheme.dimens.large))
                Converter(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    MainScreen()
}