package org.zayass.assessment.exchange.ui.mainscreen

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.zayass.assessment.exchange.ui.Screen
import org.zayass.assessment.exchange.ui.balances.Balances
import org.zayass.assessment.exchange.ui.converter.Converter
import org.zayass.assessment.exchange.ui.theme.dimens

@Composable
fun MainScreen() {
    val density = LocalDensity.current
    // workaround to emulate weight in nested columns
    var balancesHeight by remember { mutableStateOf(0.dp) }

    Screen {
        BoxWithConstraints(
            modifier = Modifier.imePadding(),
        ) {
            val totalHeight = maxHeight

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
            ) {
                Balances(
                    modifier = Modifier
                        .onSizeChanged {
                            balancesHeight = with(density) {
                                it.height.toDp()
                            }
                        }
                        .padding(
                            top = MaterialTheme.dimens.medium,
                            bottom = MaterialTheme.dimens.large,
                        ),
                )

                Converter(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(totalHeight - balancesHeight),
                )
            }
        }
    }
}
