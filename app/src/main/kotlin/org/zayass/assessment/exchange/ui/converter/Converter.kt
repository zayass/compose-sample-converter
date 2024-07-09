package org.zayass.assessment.exchange.ui.converter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.zayass.assessment.exchange.R
import org.zayass.assessment.exchange.domain.Amount
import org.zayass.assessment.exchange.domain.round
import org.zayass.assessment.exchange.ui.Header
import org.zayass.assessment.exchange.ui.ThemedSurface
import org.zayass.assessment.exchange.ui.applyPrecision
import org.zayass.assessment.exchange.ui.theme.Green40
import org.zayass.assessment.exchange.ui.theme.Red40
import org.zayass.assessment.exchange.ui.theme.dimens
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale


@Composable
fun Converter(modifier: Modifier = Modifier, viewModel: ConverterViewModel = hiltViewModel()) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Converter(state, viewModel::dispatchAction, modifier)
}

@Composable
fun Converter(
    state: UiState,
    dispatchAction: (UiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        UiState.Loading -> Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        is UiState.Ready -> ReadyConverter(state, dispatchAction, modifier)
    }
}

@Composable
fun ReadyConverter(
    state: UiState.Ready,
    dispatchAction: (UiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.showMessage) {
        ConfirmationDialog(state, dispatchAction)
    }

    Column(modifier.padding(horizontal = MaterialTheme.dimens.medium)) {
        Header(stringRes = R.string.currency_exchange)
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.small))

        SellRow(
            inputValue = state.sellValue,
            amount = state.sell,
            availableCurrencies = state.availableToSell,
            dispatchAction = dispatchAction
        )
        HorizontalDivider(modifier = Modifier.padding(start = 36.dp))
        ReceiveRow(
            inputValue = state.receiveValue,
            amount = state.receive,
            availableCurrencies = state.availableToReceive,
            dispatchAction = dispatchAction
        )

        val fee = state.fee
        if (fee != null) {
            FeeInfo(fee)
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            enabled = state.submitEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.veryLarge)
                .padding(vertical = MaterialTheme.dimens.large)
                .align(Alignment.CenterHorizontally),
            onClick = { dispatchAction(UiAction.Submit) }
        ) {
            Text(text = stringResource(R.string.submit).uppercase())
        }
    }
}

@Composable
private fun ConfirmationDialog(
    state: UiState.Ready,
    dispatchAction: (UiAction) -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = stringResource(R.string.currency_converted))
        },
        text = {
            Text(text = state.message())
        },
        onDismissRequest = {
            dispatchAction(UiAction.ConfirmDialog)
        },
        confirmButton = {
            TextButton(
                onClick = {
                    dispatchAction(UiAction.ConfirmDialog)
                }
            ) {
                Text(stringResource(id = android.R.string.ok))
            }
        }
    )
}

@Composable
fun UiState.Ready.message(): String {
    val fee = this.fee
    return if (fee != null) {
        stringResource(
            id = R.string.converted_with_fee_template,
            sell.formatFull(),
            receive.formatFull(),
            fee.formatFull()
        )
    } else {
        stringResource(
            id = R.string.converted_without_fee_template,
            sell.formatFull(),
            receive.formatFull()
        )
    }
}

@Composable
private fun SellRow(
    inputValue: String,
    amount: Amount,
    availableCurrencies: List<Currency>,
    dispatchAction: (UiAction) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowUp,
            tint = MaterialTheme.colorScheme.surface,
            contentDescription = "",
            modifier = Modifier
                .padding(vertical = MaterialTheme.dimens.small)
                .size(32.dp)
                .background(Red40, shape = CircleShape)
        )

        Spacer(modifier = Modifier.size(MaterialTheme.dimens.small))
        Text(
            text = stringResource(R.string.sell),
            fontWeight = FontWeight.Medium
        )

        AmountTextField(
            value = inputValue,
            formatedValue = stringResource(id = R.string.sell_template, amount.formatShort()),
            onValueChange = { dispatchAction(UiAction.ChangeSellAmount(it)) },
            modifier = Modifier
                .focusRequester(focusRequester)
                .weight(1f)
                .padding(horizontal = MaterialTheme.dimens.small),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Medium,
            ),
            unfocusedTextColor = Red40
        )

        DropDown(
            value = amount.currency,
            values = availableCurrencies,
            onValueChanged = { dispatchAction(UiAction.ChangeSellCurrency(it)) }
        )
    }
}

@Composable
private fun ReceiveRow(
    inputValue: String,
    amount: Amount,
    availableCurrencies: List<Currency>,
    dispatchAction: (UiAction) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.KeyboardArrowDown,
            tint = MaterialTheme.colorScheme.surface,
            contentDescription = "",
            modifier = Modifier
                .padding(vertical = MaterialTheme.dimens.small)
                .size(32.dp)
                .background(Green40, shape = CircleShape)
        )

        Spacer(modifier = Modifier.size(MaterialTheme.dimens.small))
        Text(
            text = stringResource(R.string.receive),
            fontWeight = FontWeight.Medium
        )

        AmountTextField(
            value = inputValue,
            formatedValue = stringResource(id = R.string.receive_template, amount.formatShort()),
            onValueChange = { dispatchAction(UiAction.ChangeReceiveAmount(it)) },
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = MaterialTheme.dimens.small),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.End,
                fontWeight = FontWeight.Medium,
            ),
            unfocusedTextColor = Green40
        )

        DropDown(
            value = amount.currency,
            values = availableCurrencies,
            onValueChanged = { dispatchAction(UiAction.ChangeReceiveCurrency(it)) }
        )
    }
}

@Composable
private fun FeeInfo(fee: Amount) {
    Row(Modifier.padding(top = MaterialTheme.dimens.medium)) {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = ""
        )
        Spacer(modifier = Modifier.size(MaterialTheme.dimens.small))
        Text(text = stringResource(R.string.fees_template, fee.formatFull()))
    }
}

private fun Amount.formatShort(): String {
    val numberFormat = NumberFormat.getNumberInstance(Locale.getDefault())
    numberFormat.applyPrecision(value)
    return numberFormat.format(value.round())
}

private fun Amount.formatFull(): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    numberFormat.currency = currency
    numberFormat.applyPrecision(value)
    return numberFormat.format(value.round())
}

@Preview
@Composable
private fun ConverterLoadingPreview() {
    ThemedSurface {
        Converter(
            state = UiState.Loading,
            dispatchAction = { }
        )
    }
}

@Preview
@Composable
private fun ConverterPreview() {
    ThemedSurface {
        Converter(
            state = UiState.Ready(
                submitEnabled = false,
                sell = Amount(
                    value = BigDecimal(10001).movePointLeft(2),
                    currency = Currency.getInstance("EUR")
                ),
                receive = Amount(
                    value = BigDecimal(10001).movePointLeft(2),
                    currency = Currency.getInstance("USD")
                ),
                fee = Amount(
                    value = BigDecimal(10).movePointLeft(2),
                    currency = Currency.getInstance("EUR")
                ),
                availableToSell = listOf(Currency.getInstance("USD")),
                availableToReceive = listOf(Currency.getInstance("USD")),
            ),
            dispatchAction = {}
        )
    }
}